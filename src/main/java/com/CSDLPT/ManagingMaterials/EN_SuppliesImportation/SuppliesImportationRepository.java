package com.CSDLPT.ManagingMaterials.EN_SuppliesImportation;

import com.CSDLPT.ManagingMaterials.EN_Order.Order;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SuppliesImportationRepository {
    private final Logger logger;
    private final StaticUtilMethods staticUtilMethods;

    public int save(DBConnectionHolder conHolder, SuppliesImportation importation) {
        try {
            //--Prepare data to execute Query Statement.
            PreparedStatement statement = conHolder.getConnection().prepareStatement("""
                    INSERT INTO PhieuNhap (MAPN ,MAKHO ,MasoDDH ,MANV, NGAY) VALUES (?, ?, ?, ?, ?)
                """);
            statement.setString(1, importation.getSuppliesImportationId());
            statement.setString(2, importation.getWarehouseId());
            statement.setString(3, importation.getOrderId());
            statement.setInt(4, importation.getEmployeeId());
            statement.setDate(5, staticUtilMethods.dateUtilToSqlDate(importation.getCreatedDate()));

            //--Retrieve affected rows to know if our Query worked correctly.
            int result = statement.executeUpdate();

            //--Close all connection.
            statement.close();
            return result;
        } catch (SQLException e) {
            logger.info("Error In 'save' of SuppliesImportationRepository: " + e);
            return 0;
        }
    }

    public boolean isExistingSuppliesImportationBySuppliesImportationId(DBConnectionHolder conHolder, String importationId) {
        //--Using a 'result' var to make our logic easily to control.
        boolean result = false;

        try {
            //--Prepare data to execute Stored Procedure.
            CallableStatement statement = conHolder.getConnection().prepareCall("{? = call SP_CHECK_EXIST_IMPORT_ID(?)}");
            //--Register the output parameter
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.setString(2, importationId);

            statement.execute();
            result = statement.getBoolean(1);

            //--Close all connection.
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'isExistingSuppliesImportationBySuppliesImportationId' of SuppliesImportationRepository: " + e);
        }
        return result;
    }

    public Optional<SuppliesImportation> findById(DBConnectionHolder conHolder, String suppliesImportationId) {
        Optional<SuppliesImportation> result = Optional.empty();
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("SELECT TOP 1 * FROM PhieuNhap WHERE MAPN=?");
            statement.setString(1, suppliesImportationId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                result = Optional.of(SuppliesImportation.builder()
                    .suppliesImportationId(resultSet.getString("MAPN"))
                    .warehouseId(resultSet.getString("MAKHO"))
                    .employeeId(resultSet.getInt("MANV"))
                    .orderId(resultSet.getString("MasoDDH"))
                    .createdDate(resultSet.getDate("NGAY"))
                    .build());

            //--Close all connection.
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'findById' of SuppliesImportationRepository: " + e);
        }
        return result;
    }

    public Optional<SuppliesImportation> findBySuppliesImportationIdAndEmployeeId(
        DBConnectionHolder conHolder,
        String importationId,
        Integer employeeId
    ) {
        Optional<SuppliesImportation> result = Optional.empty();
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("SELECT TOP 1 * FROM PhieuNhap WHERE MAPN=? AND MANV=?");
            statement.setString(1, importationId);
            statement.setInt(2, employeeId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                result = Optional.of(SuppliesImportation.builder()
                    .suppliesImportationId(resultSet.getString("MAPN"))
                    .warehouseId(resultSet.getString("MAKHO"))
                    .employeeId(resultSet.getInt("MANV"))
                    .orderId(resultSet.getString("MasoDDH"))
                    .createdDate(resultSet.getDate("NGAY"))
                    .build());

            //--Close all connection.
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'findBySuppliesImportationIdAndEmployeeId' of SuppliesImportationRepository: " + e);
        }
        return result;
    }

    public int updateById(DBConnectionHolder conHolder, SuppliesImportation importation) {
        int result = 0;
        try {
            PreparedStatement statement = conHolder.getConnection().prepareStatement(
                "UPDATE PhieuNhap SET MAKHO=?,MasoDDH=? WHERE MAPN=?"
            );
            statement.setString(1, importation.getWarehouseId());
            statement.setString(2, importation.getOrderId());
            statement.setString(3, importation.getSuppliesImportationId());

            result = statement.executeUpdate();

            //--Close all connection.
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'findById' of SuppliesImportationRepository: " + e);
        }
        return result;
    }

    public int deleteById(DBConnectionHolder conHolder, String importationId) {
        int result = 0;
        try {
            //--Prepare data to execute Stored Procedure.
            PreparedStatement statement = conHolder.getConnection().prepareStatement(
                "DELETE FROM PhieuNhap WHERE MAPN=?"
            );
            statement.setString(1, importationId);
            result = statement.executeUpdate();

            //--Close all connection.
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'deleteById' of SuppliesImportationRepository: " + e);
        }
        return result;
    }

    public Optional<SuppliesImportation> findByOrderId(DBConnectionHolder conHolder, String orderId) {
        Optional<SuppliesImportation> result = Optional.empty();
        try {
            PreparedStatement statement = conHolder.getConnection().prepareStatement("""
                SELECT TOP 1 * FROM PhieuNhap
                INNER JOIN (SELECT TOP 1 MasoDDH FROM DatHang WHERE MasoDDH=?) AS SimpleOrder
                ON SimpleOrder.MasoDDH = PhieuNhap.MasoDDH
            """);
            statement.setString(1, orderId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                result = Optional.of(SuppliesImportation.builder()
                    .suppliesImportationId(resultSet.getString("MAPN"))
                    .warehouseId(resultSet.getString("MAKHO"))
                    .employeeId(resultSet.getInt("MANV"))
                    .orderId(resultSet.getString("MasoDDH"))
                    .createdDate(resultSet.getDate("NGAY"))
                    .build());

            //--Close all connection.
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'findById' of SuppliesImportationRepository: " + e);
        }
        return result;
    }

    public Optional<SuppliesImportation> findByOrderIdToServeUpdate(DBConnectionHolder conHolder, String importationId, String orderId) {
        Optional<SuppliesImportation> result = Optional.empty();
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("SELECT TOP 1 * FROM PhieuNhap WHERE MasoDDH=? AND NOT MAPN=?");
            statement.setString(1, orderId);
            statement.setString(2, importationId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                result = Optional.of(SuppliesImportation.builder()
                    .suppliesImportationId(resultSet.getString("MAPN"))
                    .warehouseId(resultSet.getString("MAKHO"))
                    .employeeId(resultSet.getInt("MANV"))
                    .orderId(resultSet.getString("MasoDDH"))
                    .createdDate(resultSet.getDate("NGAY"))
                    .build());

            //--Close all connection.
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'findByOrderIdToServeUpdate' of OrderRepository: " + e);
        }
        return result;
    }
}
