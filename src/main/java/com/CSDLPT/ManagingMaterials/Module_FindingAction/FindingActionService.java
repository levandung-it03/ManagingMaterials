package com.CSDLPT.ManagingMaterials.Module_FindingAction;

import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.database.PageObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import static com.CSDLPT.ManagingMaterials.config.StaticUtilMethods.*;

@Service
@RequiredArgsConstructor
public class FindingActionService {
    private final Logger logger;
    private final StaticUtilMethods staticUtilMethods;

    /**Spring JdbcTemplate: Combination between .findingDataWithPagination(), .countAllByCondition() and JOIN Query **/
    public <T> ResDtoRetrievingData<T> findingDataAndServePaginationBarFormat(
        HttpServletRequest request,
        ReqDtoRetrievingData<T> searchingObject
    ) throws SQLException, NoSuchFieldException {
        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        //--Generate the condition syntax of query.
        String conditionOfQuery = String.format(
            "%s WHERE %s %s LIKE '%%'+?+'%%' ",
            searchingObject.getJoiningCondition().isEmpty() ? "" : searchingObject.getJoiningCondition().trim(),
            searchingObject.getMoreCondition().isEmpty() ? "" : searchingObject.getMoreCondition().trim() + " AND ",
            this.getCastedSqlDataTypeOfSearchedField(searchingObject.getSearchingField()).trim()
        );

        //--IoC here.
        String orderedFields = searchingObject.getJoiningCondition().isEmpty()
            ? "*" : this.getOrderedFieldsForJoiningQuery(searchingObject.getObjectType());
        ResDtoRetrievingData<T> resDtoRetrievingData = new ResDtoRetrievingData<>();
        resDtoRetrievingData.setResultDataSet(
            this.findingDataWithPagination(connectionHolder, searchingObject, conditionOfQuery, orderedFields));
        resDtoRetrievingData.setTotalObjectsQuantityResult(
            this.countAllByCondition(connectionHolder, searchingObject, conditionOfQuery));

        //--Close Connection.
        connectionHolder.removeConnection();

        return resDtoRetrievingData;
    }

    /**Spring JdbcTemplate: Finding data of any entities**/
    public <T> List<T> findingDataWithPagination(
        DBConnectionHolder connectionHolder,
        ReqDtoRetrievingData<T> searchingObject,
        String conditionOfQuery,
        String orderedFields
    ) {
        List<T> result = new ArrayList<>();
        try {
            PageObject pageObject = new PageObject(searchingObject.getCurrentPage());
            String query = String.format(
                "SELECT %s FROM %s %s %s OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
                orderedFields,
                searchingObject.getSearchingTable(),
                conditionOfQuery,
                searchingObject.getSortingCondition()
            );
            PreparedStatement statement = connectionHolder.getConnection().prepareStatement(query);

            //--Prepare data of employee-list.
            statement.setString(1, String.valueOf(searchingObject.getSearchingValue()));
            statement.setInt(2, (pageObject.getPage() - 1) * pageObject.getSize());
            statement.setInt(3, pageObject.getSize());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                result.add(this.mapResultSetToObject(resultSet, searchingObject.getObjectType()));

            //--Close all connection.
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'findByField' of FindingActionService: " + e);
        }
        return result;
    }

    /**Spring JdbcTemplate: Counting all entities quantity by input-condition**/
    public <T> int countAllByCondition(
        DBConnectionHolder connectionHolder,
        ReqDtoRetrievingData<T> searchingObject,
        String conditionOfQuery
    ) {
        int result = 0;
        try {
            String query = String.format(
                "SELECT SOLUONG = COUNT(%s) FROM %s %s",
                searchingObject.getSearchingTableIdName(),
                searchingObject.getSearchingTable(),
                conditionOfQuery
            );

            //--Prepare data to execute Stored Procedure.
            PreparedStatement statement = connectionHolder.getConnection().prepareStatement(query);
            statement.setString(1, searchingObject.getSearchingValue());
            ResultSet resultSet = statement.executeQuery();

            //--If at least one Employee is existing.
            if (resultSet.next()) result = resultSet.getInt("SOLUONG");

            //--Close all connection.
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'countAll' of FindingActionService: " + e);
        }
        return result;
    }

    /**Spring JdbcTemplate: This method help us map each "ResultSet" into any Models as generic type "T"**/
    public <T> T mapResultSetToObject(ResultSet resultSet, Class<T> objectType) {
        try {
            //--Get an object type "T" with all empty values of fields.
            T object = objectType.getDeclaredConstructor().newInstance();
            //--Loop through each field to set value from "resultSet".
            for (java.lang.reflect.Field field : objectType.getDeclaredFields()) {
                field.setAccessible(true);
                //--Use "Field" class to set all field's values in "object" type "T".
                field.set(object,
                    //--Get value from resultSet
                    resultSet.getObject(staticUtilMethods.columnNameStaticDictionary(field.getName()).getFirst())
                );
            }

            return object;
        } catch (Exception e) {
            logger.info("Error in 'mapResultSetToObject' of FindingActionService: " + e);
            return null;
        }
    }

    /**Spring JdbcTemplate: This method help us find the corresponding SQL data by Java Data type**/
    public String getCastedSqlDataTypeOfSearchedField(String fieldName) throws NoSuchFieldException {
        List<String> fieldInfo = staticUtilMethods.columnNameStaticDictionary(fieldName);
        //--Type-casting syntax of this query corresponding with data-type.
        return switch (fieldInfo.getLast()) {
            case NUM_TYPE -> String.format("CAST(CAST(%s AS BIGINT) AS NVARCHAR(50))", fieldInfo.getFirst());
            case DATE_TYPE -> String.format("CAST(CONVERT(DATE, %s) AS NVARCHAR(10))", fieldInfo.getFirst());
            default -> fieldInfo.getFirst();
        };
    }

    /**Spring JdbcTemplate: This method help us map the corresponding Java Obj Name to SQL column name**/
    public <T> String getOrderedFieldsForJoiningQuery(Class<T> objectType) {
        try {
            StringBuilder result = new StringBuilder();
            for (java.lang.reflect.Field field : objectType.getDeclaredFields()) {
                if (field.getName().toUpperCase().contains("FK")) {
                    result.append(staticUtilMethods.columnNameStaticDictionary(field.getName()).get(1)).append(", ");
                } else {
                    result.append(staticUtilMethods.columnNameStaticDictionary(field.getName()).getFirst()).append(", ");
                }
            }
            return result.substring(0, result.length() - 2);
        } catch (Exception e) {
            logger.info(e.toString());
            return "";
        }
    }
}
