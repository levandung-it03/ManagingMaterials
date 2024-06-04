package com.CSDLPT.ManagingMaterials.EN_Order;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final Logger logger;
    private final StaticUtilMethods staticUtilMethods;

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
