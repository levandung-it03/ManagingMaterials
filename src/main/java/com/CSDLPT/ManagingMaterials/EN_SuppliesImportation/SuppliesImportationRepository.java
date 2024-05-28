package com.CSDLPT.ManagingMaterials.EN_SuppliesImportation;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

@Repository
@RequiredArgsConstructor
public class SuppliesImportationRepository {
    private final Logger logger;
    private final StaticUtilMethods staticUtilMethods;

    public int save(DBConnectionHolder connectHolder, SuppliesImportation importation) {
        try {
            //--Prepare data to execute Query Statement.
            PreparedStatement statement = connectHolder.getConnection().prepareStatement("""
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
            logger.info("Error In 'isExistingSuppliesImportationBySuppliesImportationId' of SuppliesImportationRepository: "+e);
        }
        return result;
    }

}
