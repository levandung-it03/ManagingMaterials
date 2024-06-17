package com.CSDLPT.ManagingMaterials.EN_SuppliesExportationDetail;

import com.CSDLPT.ManagingMaterials.EN_SuppliesImportationDetail.SuppliesImportationDetail;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SuppliesExportationDetailRepository {
    private final Logger logger;

    public Optional<SuppliesExportationDetail> findById(
        DBConnectionHolder conHolder,
        String suppliesExportationId,
        String supplyId
    ) {
        Optional<SuppliesExportationDetail> result = Optional.empty();
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("SELECT TOP 1 * FROM CTPX WHERE MAPX=? AND MAVT=?");
            statement.setString(1, suppliesExportationId);
            statement.setString(2, supplyId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                result = Optional.of(SuppliesExportationDetail.builder()
                    .suppliesExportationId(resultSet.getString("MAPX"))
                    .supplyId(resultSet.getString("MAVT"))
                    .suppliesQuantity(resultSet.getInt("SOLUONG"))
                    .price(resultSet.getDouble("DONGIA"))
                    .build());

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'findById' of SuppliesExportationRepository: " + e);
        }
        return result;
    }

    public int save(DBConnectionHolder conHolder, SuppliesExportationDetail exportationDetail) {
        int result = 0;
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("INSERT INTO CTPX (MAPX, MAVT, SOLUONG, DONGIA) VALUES (?, ?, ?, ?)");
            statement.setString(1, exportationDetail.getSuppliesExportationId());
            statement.setString(2, exportationDetail.getSupplyId());
            statement.setInt(3, exportationDetail.getSuppliesQuantity());
            statement.setDouble(4, exportationDetail.getPrice());

            result = statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'save' of SuppliesExportationRepository: " + e);
        }
        return result;
    }

    public int update(DBConnectionHolder conHolder, SuppliesExportationDetail exportationDetail) {
        int result = 0;
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("UPDATE CTPX SET SOLUONG=?, DONGIA=? WHERE MAPX=? AND MAVT=?");
            statement.setInt(1, exportationDetail.getSuppliesQuantity());
            statement.setDouble(2, exportationDetail.getPrice());
            statement.setString(3, exportationDetail.getSuppliesExportationId());
            statement.setString(4, exportationDetail.getSupplyId());

            result = statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'update' of SuppliesExportationRepository: " + e);
        }
        return result;
    }

    public boolean existBySuppliesExportationId(DBConnectionHolder conHolder, String suppliesExportationId) {
        boolean result = false;
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("SELECT TOP 1 * FROM CTPX WHERE MAPX=?");
            statement.setString(1, suppliesExportationId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())     result = true;

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'existBySuppliesExportationId' of SuppliesExportationRepository: " + e);
        }
        return result;
    }

    public int saveByStoredProc(DBConnectionHolder conHolder, SuppliesExportationDetail exportationDetail) {
        try {
            CallableStatement statement = conHolder.getConnection()
                .prepareCall("{call SP_ADD_SUPPLIES_EXPORTATION_DETAIL(?, ?, ?, ?)}");
            statement.setString(1, exportationDetail.getSuppliesExportationId());
            statement.setString(2, exportationDetail.getSupplyId());
            statement.setInt(3, exportationDetail.getSuppliesQuantity());
            statement.setDouble(4, exportationDetail.getPrice());

            statement.executeUpdate();

            statement.close();

            return 1;
        } catch (Exception e) {
            logger.info("Error In 'saveByStoredProc' of SuppliesImportationRepository: " + e);
            return e.getMessage().trim().length() == 2 ? Integer.parseInt(e.getMessage().trim()) : 0;
        }
    }

    public int updateByStoredProc(DBConnectionHolder conHolder, SuppliesExportationDetail exportationDetail) {
        try {
            CallableStatement statement = conHolder.getConnection()
                .prepareCall("{call SP_UPDATE_SUPPLIES_EXPORTATION_DETAIL(?, ?, ?, ?)}");
            statement.setString(1, exportationDetail.getSuppliesExportationId());
            statement.setString(2, exportationDetail.getSupplyId());
            statement.setInt(3, exportationDetail.getSuppliesQuantity());
            statement.setDouble(4, exportationDetail.getPrice());

            statement.executeUpdate();

            statement.close();

            return 1;
        } catch (Exception e) {
            logger.info("Error In 'updateByStoredProc' of SuppliesImportationRepository: " + e);
            return e.getMessage().trim().length() == 2 ? Integer.parseInt(e.getMessage().trim()) : 0;
        }
    }
}
