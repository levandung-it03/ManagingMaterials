package com.CSDLPT.ManagingMaterials.EN_Warehouse;

import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
@RequiredArgsConstructor
public class WarehouseRepository {
    private final Logger logger;

    public int save(DBConnectionHolder connectHolder, Warehouse warehouse) {
        try {
            //--Prepare data to execute Query Statement.
            PreparedStatement statement = connectHolder.getConnection().prepareStatement("""
                INSERT INTO Kho (MAKHO ,TENKHO ,DIACHI ,MACN) VALUES (?, ?, ?, ?)
            """);
            statement.setString(1, warehouse.getWarehouseId());
            statement.setString(2, warehouse.getWarehouseName());
            statement.setString(3, warehouse.getAddress());
            statement.setString(4, warehouse.getBranch());

            //--Retrieve affected rows to know if our Query worked correctly.
            int result = statement.executeUpdate();

            //--Close all connection.
            statement.close();
            return result;
        } catch (SQLException e) {
            logger.info("Error In 'save' of WarehouseRepository: " + e);
            return 0;
        }
    }

    public int update(DBConnectionHolder connectionHolder, Warehouse warehouse) {
        try {
            PreparedStatement statement = connectionHolder.getConnection().prepareStatement("""
                UPDATE Kho SET TENKHO = ?, DIACHI = ? WHERE MAKHO = ?;
            """);
            statement.setString(1, warehouse.getWarehouseName());
            statement.setString(2, warehouse.getAddress());
            statement.setString(3, warehouse.getWarehouseId());

            //--Retrieve affected rows to know if our Query worked correctly.
            int result = statement.executeUpdate();

            //--Close all connection.
            statement.close();
            return result;
        } catch (SQLException e) {
            logger.info("Error In 'update' of WarehouseRepository: " + e);
            return 0;
        }
    }

    public boolean isExistingWarehouseByWarehouseId(DBConnectionHolder conHolder, String warehouseId) {
        //--Using a 'result' var to make our logic easily to control.
        boolean result = false;

        try {
            //--Prepare data to execute Stored Procedure.
            CallableStatement statement = conHolder.getConnection().prepareCall("{? = call SP_CHECK_EXIST_WAREHOUSE_ID(?)}");
            //--Register the output parameter
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.setString(2, warehouseId);

            statement.execute();
            result = statement.getBoolean(1);

            //--Close all connection.
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'existingWarehouseId' of WarehouseRepository: " + e);
        }
        return result;
    }

    public int delete(DBConnectionHolder connectionHolder, String warehouseId) {
        int result = 0;
        try {
            PreparedStatement statement = connectionHolder.getConnection()
                .prepareStatement("DELETE FROM Kho WHERE MAKHO = ?;");
            statement.setString(1, warehouseId);

            //--Retrieve affected rows to know if our Query worked correctly.
            result = statement.executeUpdate();

            connectionHolder.removeConnection();
        } catch (SQLException e) {
            logger.info("Error In 'delete' of WarehouseRepository: " + e);
        }
        return result;
    }

}
