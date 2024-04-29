package com.CSDLPT.ManagingMaterials.repository;

import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.model.Supply;
import com.CSDLPT.ManagingMaterials.model.PageObject;
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
public class SupplyRepository {
    private final Logger logger;

    public List<Supply> findAll(DBConnectionHolder connectHolder, PageObject pageObj) {
        //--Using a 'List<Supply>' var as our result.
        List<Supply> result = new ArrayList<>();

        try {
            //--Prepare data to execute Query Statement.
            PreparedStatement statement = connectHolder.getConnection().prepareStatement(
                    String.format("{call SP_LIST_ALL_SUPPLIES(%s, %s)}", pageObj.getPage(), pageObj.getSize())
            );
            ResultSet resultSet = statement.executeQuery();

            //--Mapping all data into 'List<Supply>' result var.
            while (resultSet.next()) {
                result.add(
                        Supply.builder()
                                .supplyId(resultSet.getString("MAVT").trim())
                                .supplyName(resultSet.getString("TENVT").trim())
                                .unit(resultSet.getString("DVT").trim())
                                .quantityInStock(resultSet.getInt("SOLUONGTON"))
                                .build()
                );
            }

            //--Close all connection.
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'findAll' of SupplyRepository: " + e);
        }
        return result;
    }
}
