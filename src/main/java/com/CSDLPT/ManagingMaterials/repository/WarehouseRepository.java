package com.CSDLPT.ManagingMaterials.repository;

import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.model.Employee;
import com.CSDLPT.ManagingMaterials.model.PageObject;
import com.CSDLPT.ManagingMaterials.model.Warehouse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WarehouseRepository {
    private final Logger logger;

    public List<Warehouse> findAll(DBConnectionHolder connectHolder, PageObject pageObj) {
        List<Warehouse> result = new ArrayList<>();
        try {
            //--Prepare data to execute Query Statement.
            PreparedStatement statement = connectHolder.getConnection().prepareStatement(
                String.format("{call SP_LIST_ALL_WAREHOUSES(%s, %s)}", pageObj.getPage(), pageObj.getSize())
            );
            ResultSet resultSet = statement.executeQuery();

            //--Mapping all data into 'List<Warehouse>' result var.
            while (resultSet.next()) {
                result.add(
                    Warehouse.builder()
                        .warehouseId(resultSet.getString("MAKHO").trim())
                        .warehouseName(resultSet.getString("TENKHO").trim())
                        .address(resultSet.getString("DIACHI").trim())
                        .branch(resultSet.getString("MACN").trim())
                        .build()
                );
            }

            //--Close all connection.
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'findAll' of WarehouseRepository: " + e);
        }
        return result;
    }

    public int save(DBConnectionHolder connectHolder, Warehouse warehouse) {
        try {
            //--Prepare data to execute Query Statement.
            PreparedStatement statement = connectHolder.getConnection().prepareStatement("""
                INSERT INTO Kho (MAKHO ,TENKHO ,DIACHI ,MACN) VALUES (?, ?, ?, ?)
            """);
            this.mapDataIntoStatement(statement, warehouse);

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

    public void mapDataIntoStatement(PreparedStatement statement, Warehouse warehouse) throws SQLException {
        statement.setString(1, warehouse.getWarehouseId());
        statement.setString(2, warehouse.getWarehouseName());
        statement.setString(3, warehouse.getAddress());
        statement.setString(4, warehouse.getBranch());
    }
}
