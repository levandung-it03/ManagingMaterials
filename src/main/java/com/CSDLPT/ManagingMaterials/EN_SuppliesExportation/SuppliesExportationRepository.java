package com.CSDLPT.ManagingMaterials.EN_SuppliesExportation;

import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SuppliesExportationRepository {
    private final Logger logger;

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
}
