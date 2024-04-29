package com.CSDLPT.ManagingMaterials.repository;

import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.model.PageObject;
import com.CSDLPT.ManagingMaterials.model.Warehouse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

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
}
