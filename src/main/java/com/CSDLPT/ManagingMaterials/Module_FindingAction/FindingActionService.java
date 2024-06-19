package com.CSDLPT.ManagingMaterials.Module_FindingAction;

import com.CSDLPT.ManagingMaterials.EN_Account.RoleEnum;
import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.database.PageObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${mssql.database.name}")
    private String databaseName;

    /**Spring JdbcTemplate: Combination between .findingDataWithPagination(), .countAllByCondition() and JOIN Query **/
    public <T> ResDtoRetrievingData<T> findingDataAndServePaginationBarFormat(
        HttpServletRequest request,
        ReqDtoRetrievingData<T> searchingObject
    ) throws SQLException, NoSuchFieldException {
        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
        searchingObject.trimAllDataFields();

        //--Find searching branch
        ResDtoUserInfo userInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");
        if (userInfo.getRole().equals(RoleEnum.CONGTY))
            if (!searchingObject.getBranch().isEmpty() && !userInfo.getBranch().equals(searchingObject.getBranch()))
                searchingObject.setSearchingTable("LINK1." + databaseName + ".DBO." + searchingObject.getSearchingTable());

        //--Generate the condition syntax of query.
        String conditionOfQuery = String.format(
            "%s WHERE %s %s LIKE N'%%'+?+'%%' ",
            searchingObject.getJoiningCondition().isEmpty() ? "" : searchingObject.getJoiningCondition().trim(),
            searchingObject.getMoreCondition().isEmpty() ? "" : searchingObject.getMoreCondition().trim() + " AND ",
            this.getCastedSqlDataTypeOfSearchedField(
                searchingObject.getSearchingField(),
                searchingObject.getObjectType()
            ).trim()
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
                "SELECT %s FROM %s %s %s %s",
                orderedFields,
                searchingObject.getSearchingTable(),
                conditionOfQuery,
                searchingObject.getSortingCondition(),
                pageObject.getPage() == 0 ? "" : " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
            );
            PreparedStatement statement = connectionHolder.getConnection().prepareStatement(query);

            //--Prepare data of employee-list.
            statement.setString(1, String.valueOf(searchingObject.getSearchingValue()));
            if (pageObject.getPage() != 0) {
                statement.setInt(2, (pageObject.getPage() - 1) * pageObject.getSize());
                statement.setInt(3, pageObject.getSize());
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                result.add(this.mapResultSetToObject(resultSet, searchingObject.getObjectType()));

            //--Close all connection.
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'findingDataWithPagination' of FindingActionService: " + e);
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
    public <T> String getCastedSqlDataTypeOfSearchedField(String fieldName, Class<T> objectType) throws NoSuchFieldException {
        String tableName = "", objType = objectType.getSimpleName();
        try { tableName = staticUtilMethods.columnNameStaticDictionary(objType).getFirst() + "."; }
        catch (Exception ignored) {}

        List<String> fieldInfo = staticUtilMethods.columnNameStaticDictionary(fieldName);
        String sqlFieldName;
        if (fieldName.toUpperCase().contains("FK"))
            sqlFieldName = staticUtilMethods.columnNameStaticDictionary(fieldName).get(1);
        else {
            sqlFieldName = staticUtilMethods.columnNameStaticDictionary(fieldName).getFirst();
            //--Ignoring fields which is defined as SQL methods.
            if (!sqlFieldName.contains("(") && !sqlFieldName.contains(".") && !sqlFieldName.contains(")"))
                sqlFieldName = tableName + sqlFieldName;
        }

        //--Type-casting syntax of this query corresponding with data-type.
        return switch (fieldInfo.getLast()) {
            case NUM_TYPE -> String.format("CAST(CAST(%s AS BIGINT) AS NVARCHAR(50))", sqlFieldName);
            case DATE_TYPE -> String.format("CAST(CONVERT(DATE, %s) AS NVARCHAR(10))", sqlFieldName);
            default -> sqlFieldName;
        };
    }

    /**Spring JdbcTemplate: This method help us map the corresponding Java Obj Name to SQL column name**/
    public <T> String getOrderedFieldsForJoiningQuery(Class<T> objectType) {
        try {
            String tableName = "", objType = objectType.getSimpleName();
            try { tableName = staticUtilMethods.columnNameStaticDictionary(objType).getFirst() + "."; }
            catch (Exception ignored) {}

            StringBuilder result = new StringBuilder();
            for (java.lang.reflect.Field field : objectType.getDeclaredFields()) {
                if (field.getName().toUpperCase().contains("FK")) {
                    result.append(staticUtilMethods.columnNameStaticDictionary(field.getName()).get(1)).append(", ");
                } else {
                    String fieldNameInSQL = staticUtilMethods.columnNameStaticDictionary(field.getName()).getFirst();
                    //--Ignoring fields which is defined as SQL methods.
                    if (!fieldNameInSQL.contains("(") && !fieldNameInSQL.contains(".") && !fieldNameInSQL.contains(")"))
                        result.append(tableName);

                    result.append(fieldNameInSQL).append(", ");
                }
            }
            return result.substring(0, result.length() - 2);
        } catch (Exception e) {
            logger.info(e.toString());
            return "";
        }
    }
}
