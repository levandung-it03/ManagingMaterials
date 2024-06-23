package com.CSDLPT.ManagingMaterials.EN_SuppliesExportation;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SuppliesExportationRepository {
    private final Logger logger;
    private final StaticUtilMethods staticUtilMethods;

    public Optional<SuppliesExportation> findById(DBConnectionHolder conHolder, String exportationId) {
        Optional<SuppliesExportation> result = Optional.empty();
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("SELECT TOP 1 * FROM PhieuXuat WHERE MAPX=?");
            statement.setString(1, exportationId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                result = Optional.of(SuppliesExportation.builder()
                    .suppliesExportationId(resultSet.getString("MAPX"))
                    .warehouseId(resultSet.getString("MAKHO"))
                    .employeeId(resultSet.getInt("MANV"))
                    .customerFullName(resultSet.getString("HOTENKH"))
                    .createdDate(resultSet.getDate("NGAY"))
                    .build());

            //--Close all connection.
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'findById' of SuppliesExportationRepository: " + e);
        }
        return result;
    }

    public Optional<SuppliesExportation> findBySuppliesExportationIdAndEmployeeId(
        DBConnectionHolder conHolder,
        String exportationId,
        Integer employeeId
    ) {
        Optional<SuppliesExportation> result = Optional.empty();
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("SELECT TOP 1 * FROM PhieuXuat WHERE MAPX=? AND MANV=?");
            statement.setString(1, exportationId);
            statement.setInt(2, employeeId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                result = Optional.of(SuppliesExportation.builder()
                    .suppliesExportationId(resultSet.getString("MAPX"))
                    .warehouseId(resultSet.getString("MAKHO"))
                    .employeeId(resultSet.getInt("MANV"))
                    .customerFullName(resultSet.getString("HOTENKH"))
                    .createdDate(resultSet.getDate("NGAY"))
                    .build());

            //--Close all connection.
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'findBySuppliesExportationIdAndEmployeeId' of SuppliesExportationRepository: " + e);
        }
        return result;
    }

    public boolean isExistingSuppliesExportationBySuppliesExportationId(DBConnectionHolder conHolder, String exportationId) {
        //--Using a 'result' var to make our logic easily to control.
        boolean result = false;
        try {
            //--Prepare data to execute Stored Procedure.
            CallableStatement statement = conHolder.getConnection().prepareCall("{? = call SP_CHECK_EXIST_EXPORT_ID(?)}");
            //--Register the output parameter
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.setString(2, exportationId);

            statement.execute();
            result = statement.getBoolean(1);

            //--Close all connection.
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'isExistingSuppliesExportationBySuppliesExportationId' of SuppliesExportationRepository: " + e);
        }
        return result;
    }

    public int save(DBConnectionHolder conHolder, SuppliesExportation exportation) {
        int result = 0;
        try {
            PreparedStatement statement = conHolder.getConnection().
                prepareStatement("INSERT INTO PhieuXuat (MAPX, HOTENKH, MANV, MAKHO, NGAY) VALUES (?, ?, ?, ?, ?);");
            statement.setString(1, exportation.getSuppliesExportationId());
            statement.setString(2, exportation.getCustomerFullName());
            statement.setInt(3, exportation.getEmployeeId());
            statement.setString(4, exportation.getWarehouseId());
            statement.setDate(5, staticUtilMethods.dateUtilToSqlDate(exportation.getCreatedDate()));

            result = statement.executeUpdate();

            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'save' of SuppliesExportationRepository: " + e);
        }
        return result;
    }

    public int updateById(DBConnectionHolder conHolder, SuppliesExportation exportation) {
        int result = 0;
        try {
            PreparedStatement statement = conHolder.getConnection().
                prepareStatement("UPDATE PhieuXuat SET HOTENKH=?, MAKHO=? WHERE MAPX=?;");
            statement.setString(1, exportation.getCustomerFullName());
            statement.setString(2, exportation.getWarehouseId());
            statement.setString(3, exportation.getSuppliesExportationId());

            result = statement.executeUpdate();

            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'updateById' of SuppliesExportationRepository: " + e);
        }
        return result;
    }

    public int deleteById(DBConnectionHolder conHolder, String exportationId) {
        int result = 0;
        try {
            PreparedStatement statement = conHolder.getConnection().
                prepareStatement("DELETE FROM PhieuXuat WHERE MAPX=?;");
            statement.setString(1, exportationId);

            result = statement.executeUpdate();

            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'updateById' of SuppliesExportationRepository: " + e);
        }
        return result;
    }
}
