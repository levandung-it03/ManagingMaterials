package com.CSDLPT.ManagingMaterials.EN_SuppliesImportationDetail;

import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SuppliesImportationDetailRepository {
    private final Logger logger;

    public Optional<SuppliesImportationDetail> findById(
        DBConnectionHolder conHolder,
        String suppliesImportationId,
        String supplyId
    ) {
        Optional<SuppliesImportationDetail> result = Optional.empty();
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("SELECT TOP 1 * FROM CTPN WHERE MAPN=? AND MAVT=?");
            statement.setString(1, suppliesImportationId);
            statement.setString(2, supplyId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                result = Optional.of(SuppliesImportationDetail.builder()
                    .suppliesImportationId(resultSet.getString("MAPN"))
                    .supplyId(resultSet.getString("MAVT"))
                    .suppliesQuantity(resultSet.getInt("SOLUONG"))
                    .price(resultSet.getDouble("DONGIA"))
                    .build());

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'findById' of SuppliesImportationRepository: " + e);
        }
        return result;
    }

    public int save(DBConnectionHolder conHolder, SuppliesImportationDetail importationDetail) {
        int result = 0;
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("INSERT INTO CTPN (MAPN, MAVT, SOLUONG, DONGIA) VALUES (?, ?, ?, ?)");
            statement.setString(1, importationDetail.getSuppliesImportationId());
            statement.setString(2, importationDetail.getSupplyId());
            statement.setInt(3, importationDetail.getSuppliesQuantity());
            statement.setDouble(4, importationDetail.getPrice());

            result = statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'save' of SuppliesImportationRepository: " + e);
        }
        return result;
    }

    public int update(DBConnectionHolder conHolder, SuppliesImportationDetail importationDetail) {
        int result = 0;
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("UPDATE CTPN SET SOLUONG=?, DONGIA=? WHERE MAPN=? AND MAVT=?");
            statement.setInt(1, importationDetail.getSuppliesQuantity());
            statement.setDouble(2, importationDetail.getPrice());
            statement.setString(3, importationDetail.getSuppliesImportationId());
            statement.setString(4, importationDetail.getSupplyId());

            result = statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'update' of SuppliesImportationRepository: " + e);
        }
        return result;
    }

    public int delete(DBConnectionHolder conHolder, String importationDetailId, String supplyId) {
        int result = 0;
        try {
            PreparedStatement statement = conHolder.getConnection()
                .prepareStatement("DELETE FROM CTPN WHERE MAPN=? AND MAVT=?");
            statement.setString(1, importationDetailId);
            statement.setString(2, supplyId);

            result = statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            logger.info("Error In 'delete' of SuppliesImportationRepository: " + e);
        }
        return result;
    }
}
