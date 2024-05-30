package com.CSDLPT.ManagingMaterials.EN_Order;

import com.CSDLPT.ManagingMaterials.EN_SuppliesImportation.SuppliesImportation;
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

}
