package com.CSDLPT.ManagingMaterials.EN_Order;

import com.CSDLPT.ManagingMaterials.EN_Employee.dtos.ReqDtoReportForEmployeeActivities;
import com.CSDLPT.ManagingMaterials.EN_Employee.dtos.ResDtoReportForEmployeeActivities;
import com.CSDLPT.ManagingMaterials.EN_Order.dtos.ResDtoOrderWithImportantInfo;
import com.CSDLPT.ManagingMaterials.EN_Order.dtos.ResDtoReportForOrderDontHaveImport;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.FindingActionService;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.database.PageObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final Logger logger;
    private final StaticUtilMethods staticUtilMethods;
    private final FindingActionService findingActionService;

    public boolean isExistingOrderByOrderId(DBConnectionHolder conHolder, String orderId) {
        //--Using a 'result' var to make our logic easily to control.
        boolean result = false;

        try {
            //--Prepare data to execute Stored Procedure.
            CallableStatement statement = conHolder.getConnection().prepareCall("{? = call SP_CHECK_EXIST_ORDER_ID(?)}");
            statement.setString(2, orderId);

            //--Register the output parameter
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.execute();
            //--Return 1(true) if orderId is already exist
            result = statement.getBoolean(1);

            //--Close all connection.
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'isExistingOrderByOrderId' of OrderRepository: " + e);
        }
        return result;
    }

    public Optional<Order> findById(DBConnectionHolder conHolder, String orderId) {
        Optional<Order> result = Optional.empty();
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("SELECT TOP 1 * FROM DatHang WHERE MasoDDH=?");
            statement.setString(1, orderId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                result = Optional.of(Order.builder()
                    .orderId(resultSet.getString("MasoDDH"))
                    .warehouseId(resultSet.getString("MAKHO"))
                    .supplier(resultSet.getString("NhaCC"))
                    .employeeId(resultSet.getInt("MANV"))
                    .createdDate(resultSet.getDate("NGAY"))
                    .build());

            //--Close all connection.
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'findById' of OrderRepository: " + e);
        }
        return result;
    }

    public Optional<Order> findByOrderIdAndEmployeeId(DBConnectionHolder conHolder, String orderId, Integer employeeId) {
        Optional<Order> result = Optional.empty();
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("SELECT TOP 1 * FROM DatHang WHERE MasoDDH=? AND MANV=?");
            statement.setString(1, orderId);
            statement.setInt(2, employeeId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                result = Optional.of(Order.builder()
                    .orderId(resultSet.getString("MasoDDH"))
                    .warehouseId(resultSet.getString("MAKHO"))
                    .supplier(resultSet.getString("NhaCC"))
                    .employeeId(resultSet.getInt("MANV"))
                    .createdDate(resultSet.getDate("NGAY"))
                    .build());

            //--Close all connection.
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'findByEmployeeId' of OrderRepository: " + e);
        }
        return result;
    }

    public ResDtoRetrievingData<ResDtoReportForOrderDontHaveImport> findAllOrderDontHaveImport(
        DBConnectionHolder connectHolder,
        ReqDtoRetrievingData<ResDtoOrderWithImportantInfo> searchingObject
    ) throws NoSuchFieldException {
        ResDtoRetrievingData<ResDtoReportForOrderDontHaveImport> resDtoRetrievingData = new ResDtoRetrievingData<>();
        try {
            List<ResDtoReportForOrderDontHaveImport> resultList = new java.util.ArrayList<>(List.of());
            PageObject pageObject = new PageObject(searchingObject.getCurrentPage());
            int offset = (pageObject.getPage() - 1) * pageObject.getSize();
            String conditionOfQuery = String.format(
                "%s %s LIKE N'%%%s%%' ",
                searchingObject.getMoreCondition().isEmpty() ? "" : searchingObject.getMoreCondition().trim() + " AND ",
                findingActionService.getCastedSqlDataTypeOfSearchedField(
                    searchingObject.getSearchingField(),
                    searchingObject.getObjectType()
                ).trim(),
                searchingObject.getSearchingValue()
            );

            //--Merge condition,sort and paging into 1 string for SP parameter
            StringBuilder moreConditionBuilder = new StringBuilder(conditionOfQuery);
            if (pageObject.getPage() != 0) {
                moreConditionBuilder.append(searchingObject.getSortingCondition())
                    .append(" OFFSET ").append(offset)
                    .append(" ROWS FETCH NEXT ").append(pageObject.getSize())
                    .append(" ROWS ONLY");
            }
            String moreCondition = moreConditionBuilder.toString();

            //--Prepare data to execute Query Statement.
            CallableStatement statement = connectHolder.getConnection()
                .prepareCall("{call SP_FIND_ALL_ORDER_DONT_HAVE_IMPORT(?)}");
            statement.setString(1, moreCondition);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                resultList.add(ResDtoReportForOrderDontHaveImport.builder()
                    .orderId(resultSet.getString("MasoDDH"))
                    .createdDate(resultSet.getDate("NGAY"))
                    .supplier(resultSet.getString("NhaCC"))
                    .employeeFullName(resultSet.getString("HOTEN"))
                    .supplyName(resultSet.getString("TENVT"))
                    .suppliesQuantity(resultSet.getString("SOLUONG"))
                    .price(resultSet.getDouble("DONGIA"))
                    .build());
            }
            resDtoRetrievingData.setResultDataSet(resultList);

            //--Get total record number without paging
            statement.setString(1, conditionOfQuery);
            ResultSet resultSetWithoutPaging = statement.executeQuery();
            int rowCount = 0;
            while (resultSetWithoutPaging.next()) rowCount++;
            resDtoRetrievingData.setTotalObjectsQuantityResult(rowCount);

            //--Close all connection.
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'findAllOrderDontHaveImport' of OrderRepository: " + e);
        }
        return resDtoRetrievingData;
    }

    public int save(DBConnectionHolder conHolder, Order order) {
        try {
            //--Prepare data to execute Query Statement.
            PreparedStatement statement = conHolder.getConnection().prepareStatement("""
                    INSERT INTO DatHang (MasoDDH ,NGAY ,NhaCC ,MANV, MAKHO) VALUES (?, ?, ?, ?, ?)
                """);
            statement.setString(1, order.getOrderId());
            statement.setDate(2, staticUtilMethods.dateUtilToSqlDate(order.getCreatedDate()));
            statement.setString(3, order.getSupplier());
            statement.setInt(4, order.getEmployeeId());
            statement.setString(5, order.getWarehouseId());

            //--Retrieve affected rows to know if our Query worked correctly.
            int result = statement.executeUpdate();

            //--Close all connection.
            statement.close();
            return result;
        } catch (SQLException e) {
            logger.info("Error In 'save' of OrderRepository: " + e);
            return 0;
        }
    }

    public int updateById(DBConnectionHolder conHolder, Order order) {
        int result = 0;
        try {
            PreparedStatement statement = conHolder.getConnection().
                prepareStatement("UPDATE DatHang SET NhaCC=?, MAKHO=? WHERE MasoDDH=?;");
            statement.setString(1, order.getSupplier());
            statement.setString(2, order.getWarehouseId());
            statement.setString(3, order.getOrderId());

            result = statement.executeUpdate();

            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'updateById' of OrderRepository: " + e);
        }
        return result;
    }

    public int deleteById(DBConnectionHolder conHolder, String orderId) {
        int result = 0;
        try {
            PreparedStatement statement = conHolder.getConnection().
                prepareStatement("DELETE FROM DatHang WHERE MasoDDH=?;");
            statement.setString(1, orderId);

            result = statement.executeUpdate();

            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'updateById' of SuppliesExportationRepository: " + e);
        }
        return result;
    }
}
