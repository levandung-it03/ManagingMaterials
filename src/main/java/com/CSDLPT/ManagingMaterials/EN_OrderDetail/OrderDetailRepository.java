package com.CSDLPT.ManagingMaterials.EN_OrderDetail;

import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
@Repository
@RequiredArgsConstructor
public class OrderDetailRepository {
    private final Logger logger;
    public Optional<OrderDetail> findById(
        DBConnectionHolder conHolder,
        String orderId,
        String supplyId
    ) {
        Optional<OrderDetail> result = Optional.empty();
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("SELECT TOP 1 * FROM CTDDH WHERE MasoDDH=? AND MAVT=?");
            statement.setString(1, orderId);
            statement.setString(2, supplyId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                result = Optional.of(OrderDetail.builder()
                    .orderId(resultSet.getString("MasoDDH"))
                    .supplyId(resultSet.getString("MAVT"))
                    .suppliesQuantity(resultSet.getInt("SOLUONG"))
                    .price(resultSet.getDouble("DONGIA"))
                    .build());

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'findById' of OrderRepository: " + e);
        }
        return result;
    }

    public int save(DBConnectionHolder conHolder, OrderDetail orderDetail) {
        int result = 0;
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("INSERT INTO CTDDH (MasoDDH, MAVT, SOLUONG, DONGIA) VALUES (?, ?, ?, ?)");
            statement.setString(1, orderDetail.getOrderId());
            statement.setString(2, orderDetail.getSupplyId());
            statement.setInt(3, orderDetail.getSuppliesQuantity());
            statement.setDouble(4, orderDetail.getPrice());

            result = statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'save' of OrderRepository: " + e);
        }
        return result;
    }

    public int update(DBConnectionHolder conHolder, OrderDetail orderDetail) {
        int result = 0;
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("UPDATE CTDDH SET SOLUONG=?, DONGIA=? WHERE MasoDDH=? AND MAVT=?");
            statement.setInt(1, orderDetail.getSuppliesQuantity());
            statement.setDouble(2, orderDetail.getPrice());
            statement.setString(3, orderDetail.getOrderId());
            statement.setString(4, orderDetail.getSupplyId());

            result = statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'update' of OrderRepository: " + e);
        }
        return result;
    }

    public int delete(DBConnectionHolder conHolder, String orderDetailId, String supplyId) {
        int result = 0;
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("DELETE FROM CTDDH WHERE MasoDDH=? AND MAVT=?");
            statement.setString(1, orderDetailId);
            statement.setString(2, supplyId);

            result = statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'delete' of OrderRepository: " + e);
        }
        return result;
    }
}
