package com.CSDLPT.ManagingMaterials.service.GeneralService;

import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.dto.ReqDtoFindingAction;
import com.CSDLPT.ManagingMaterials.model.PageObject;
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

@Service
@RequiredArgsConstructor
public class FindingActionService {
    private final Logger logger;
    private final StaticUtilMethods staticUtilMethods;

    /**Spring JdbcTemplate: Finding data of any entities**/
    public <T> List<T> findingDataWithPaging(
        HttpServletRequest request,
        ReqDtoFindingAction<T> searchingObject
    ) {
        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
        List<T> result = new ArrayList<>();
        try {
            PageObject pageObject = new PageObject(request);
            String query = "SELECT * FROM " + searchingObject.getSearchingTable()
                + " WHERE " + searchingObject.getMoreCondition() + (searchingObject.getMoreCondition().isEmpty() ? " " : " AND ")
                + searchingObject.getSearchingField() + " LIKE '%' + ? +'%' "
                + searchingObject.getSortingCondition() + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            PreparedStatement statement = connectionHolder.getConnection().prepareStatement(query);

            //--Prepare data of employee-list.
            statement.setString(1, searchingObject.getSearchingValue());
            statement.setInt(2, (pageObject.getPage() - 1) * pageObject.getSize());
            statement.setInt(3, pageObject.getSize());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                result.add(this.mapResultSetToObject(resultSet, searchingObject.getObjectType()));

            //--Close all connection.
            resultSet.close();
            statement.close();
            connectionHolder.removeConnection();
        } catch (SQLException e) {
            logger.info("Error In 'findByField' of EmployeeRepository: " + e);
        }
        return result;
    }

    /**Spring JdbcTemplate: This method help us to map each result set to any models as generic type "T"**/
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
                    resultSet.getObject(
                        staticUtilMethods.columnNameStaticDictionary(field.getName())
                    )
                );
            }

            return object;
        } catch (Exception e) {
            return null;
        }
    }
}
