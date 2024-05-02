package com.CSDLPT.ManagingMaterials.repository;

import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.model.Supply;
import com.CSDLPT.ManagingMaterials.model.PageObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
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

    public boolean isExistingSupplyBySupplyId(DBConnectionHolder conHolder, String supplyId) {
        //--Using a 'result' var to make our logic easily to control.
        boolean result = false;

        try {
            //--Prepare data to execute Stored Procedure.
            CallableStatement statement = conHolder.getConnection().prepareCall("{? = call SP_CHECK_EXIST_SUPPLY_ID(?)}");
            statement.setString(2, supplyId);

            //--Register the output parameter
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.execute();
            //--Return 1(true) if supplyId is already exist
            result = statement.getBoolean(1);

            //--Close all connection.
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'existingSupplyId' of SupplyRepository: " + e);
        }
        return result;
    }

    public int save(DBConnectionHolder connectHolder, Supply supply) {
        try {
            //--Prepare data to execute Query Statement.
            PreparedStatement statement = connectHolder.getConnection().prepareStatement("""
                INSERT INTO VATTU (MAVT ,TENVT ,DVT ,SOLUONGTON)
                VALUES (?, ?, ?, ?)
            """);
            this.mapDataIntoStatement(statement, supply);

            //--Retrieve affected rows to know if our Query worked correctly.
            int result = statement.executeUpdate();

            //--Close all connection.
            statement.close();
            return result;
        } catch (SQLException e) {
            logger.info("Error In 'save' of SupplyRepository: " + e);
            return 0;
        }
    }

//    public int update(DBConnectionHolder connectionHolder, Supply supply, String oldBranch) {
//        try {
//            CallableStatement statement = connectionHolder.getConnection()
//                    .prepareCall("{call SP_UPDATE_SUPPLY_QUANTITY(?, ?, ?)}");
//            this.mapDataIntoStatement(statement, supply);
//            statement.setString(9, oldBranch);
//
//            //--Retrieve affected rows to know if our Query worked correctly.
//            int result = statement.executeUpdate();
//
//            //--Close all connection.
//            statement.close();
//            return result;
//        } catch (SQLException e) {
//            logger.info("Error In 'update' of SupplyRepository: " + e);
//            return 0;
//        }
//    }

    /**
     * SQL Server: This method is used to map data into the common PreparedStatement, which has all fields of Supply.
     **/
    public void mapDataIntoStatement(PreparedStatement statement, Supply supply) throws SQLException {
        statement.setString(1, supply.getSupplyId());
        statement.setString(2, supply.getSupplyName());
        statement.setString(3, supply.getUnit());
        statement.setInt(4, supply.getQuantityInStock());
    }
}