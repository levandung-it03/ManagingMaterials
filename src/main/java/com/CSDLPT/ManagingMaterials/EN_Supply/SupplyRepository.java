package com.CSDLPT.ManagingMaterials.EN_Supply;

import com.CSDLPT.ManagingMaterials.EN_Account.RoleEnum;
import com.CSDLPT.ManagingMaterials.EN_Supply.dtos.ReqDtoTicketsForDetailSuppliesReport;
import com.CSDLPT.ManagingMaterials.EN_Supply.dtos.ResDtoTicketsForDetailSuppliesReport;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
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
    private final StaticUtilMethods staticUtilMethods;

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
            logger.info("Error In 'isExistingSupplyBySupplyId' of SupplyRepository: " + e);
        }
        return result;
    }

    public boolean isUsingSupplyBySupplyId(DBConnectionHolder connectHolder, String supplyId) {
        //--Using a 'result' var to make our logic easily to control.
        boolean result = false;

        try {
            //--Prepare data to execute Stored Procedure.
            CallableStatement statement = connectHolder.getConnection().prepareCall("{? = call SP_CHECK_USING_SUPPLY(?)}");
            statement.setString(2, supplyId);

            //--Register the output parameter
            statement.registerOutParameter(1, Types.BOOLEAN);
            statement.execute();
            //--Return 1(true) if supplyId is already used
            result = statement.getBoolean(1);

            //--Close all connection.
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'isUsingSupplyBySupplyId' of SupplyRepository: " + e);
        }
        return result;
    }

    public int save(DBConnectionHolder connectHolder, Supply supply) {
        try {
            //--Prepare data to execute Query Statement.
            PreparedStatement statement = connectHolder.getConnection().prepareStatement("""
                    INSERT INTO Vattu (MAVT ,TENVT ,DVT ,SOLUONGTON)
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

    public int update(DBConnectionHolder connectionHolder, Supply supply) {
        try {
            PreparedStatement statement = connectionHolder.getConnection().prepareStatement("""
                    UPDATE Vattu SET TENVT = ?, DVT = ?, SOLUONGTON = ? WHERE MAVT = ?;
                """);
            statement.setString(1, supply.getSupplyName());
            statement.setString(2, supply.getUnit());
            statement.setInt(3, supply.getQuantityInStock());
            statement.setString(4, supply.getSupplyId());

            //--Retrieve affected rows to know if our Query worked correctly.
            int result = statement.executeUpdate();

            //--Close all connection.
            statement.close();
            return result;
        } catch (SQLException e) {
            logger.info("Error In 'update' of SupplyRepository: " + e);
            return 0;
        }
    }

    public int delete(DBConnectionHolder connectionHolder, String supplyId) {
        int result = 0;
        try {
            PreparedStatement statement = connectionHolder.getConnection()
                .prepareStatement("DELETE FROM Vattu WHERE MAVT = ?;");
            statement.setString(1, supplyId);

            //--Retrieve affected rows to know if our Query worked correctly.
            result = statement.executeUpdate();

            connectionHolder.removeConnection();
        } catch (SQLException e) {
            logger.info("Error In 'delete' of SupplyRepository: " + e);
        }
        return result;
    }

    public List<ResDtoTicketsForDetailSuppliesReport> findTicketsForDetailSuppliesReport(
        DBConnectionHolder connectHolder,
        ReqDtoTicketsForDetailSuppliesReport requiredInfoToSearchDetailSupplies,
        Integer employeeId
    ) {
        List<ResDtoTicketsForDetailSuppliesReport> resultList = new ArrayList<>();
        try {
            //--Prepare data to execute Query Statement.
            CallableStatement statement = connectHolder.getConnection()
                .prepareCall("{call SP_FIND_TICKETS_FOR_DETAIL_SUPPLIES_REPORT(?, ?, ?, ?)}");

            statement.setString(1, requiredInfoToSearchDetailSupplies.getTicketsType());
            statement.setDate(2, staticUtilMethods
                .dateUtilToSqlDate(requiredInfoToSearchDetailSupplies.getStartingDate()));
            statement.setDate(3, staticUtilMethods
                .dateUtilToSqlDate(requiredInfoToSearchDetailSupplies.getEndingDate()));
            statement.setString(4, employeeId.toString());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                resultList.add(ResDtoTicketsForDetailSuppliesReport.builder()
                    .month(resultSet.getString("THANG"))
                    .supplyName(resultSet.getString("TENVT"))
                    .totalSuppliesQuantity(resultSet.getInt("TONGSOLUONG"))
                    .totalPrices(resultSet.getDouble("TONGTRIGIA"))
                    .build());
            }

            //--Close all connection.
            statement.close();
        } catch (SQLException e) {
            logger.info("Error In 'findTicketsForDetailSuppliesReport' of SupplyRepository: " + e);
        }
        return resultList;
    }

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
