package com.CSDLPT.ManagingMaterials.EN_SuppliesExportationDetail;

import com.CSDLPT.ManagingMaterials.EN_SuppliesExportationDetail.SuppliesExportationDetail;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public int delete(DBConnectionHolder conHolder, String exportationDetailId, String supplyId) {
        int result = 0;
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("DELETE FROM CTPX WHERE MAPX=? AND MAVT=?");
            statement.setString(1, exportationDetailId);
            statement.setString(2, supplyId);

            result = statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'delete' of SuppliesExportationRepository: " + e);
        }
        return result;
    }
}