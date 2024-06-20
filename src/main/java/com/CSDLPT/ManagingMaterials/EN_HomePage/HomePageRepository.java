package com.CSDLPT.ManagingMaterials.EN_HomePage;

import com.CSDLPT.ManagingMaterials.EN_HomePage.dto.InventoryPercentageDto;
import com.CSDLPT.ManagingMaterials.EN_HomePage.dto.SupplyTrendDto;
import com.CSDLPT.ManagingMaterials.EN_HomePage.dto.TotalImportAndExportOfYearDto;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Repository
@RequiredArgsConstructor
public class HomePageRepository {
    private final Logger logger;

    public double calculateTotalImport(HttpServletRequest request) {
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
        double result = -1;
        try {
            PreparedStatement statement = connectionHolder.getConnection()
                    .prepareStatement("SELECT TONG_NHAP = SUM(CTPN.SOLUONG * CTPN.DONGIA) FROM CTPN");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getDouble("TONG_NHAP");
            }

            //--Close all connection.
            resultSet.close();
            statement.close();
        }
        catch (Exception exception) {
            logger.info("Error In 'calculateTotalImport' of HomePageRepository: " + exception);
        }
        return result;
    }

    public double calculateTotalExport(HttpServletRequest request) {
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
        double result = -1;
        try {
            PreparedStatement statement = connectionHolder.getConnection()
                    .prepareStatement("SELECT TONG_XUAT = SUM(CTPX.SOLUONG * CTPX.DONGIA) FROM CTPX");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getDouble("TONG_XUAT");
            }

            //--Close all connection.
            resultSet.close();
            statement.close();
        }
        catch (Exception exception) {
            logger.info("Error In 'calculateTotalImport' of HomePageRepository: " + exception);
        }
        return result;
    }

    public InventoryPercentageDto calculateInventoryPercentage(HttpServletRequest request) {
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
        InventoryPercentageDto result = new InventoryPercentageDto();
        try {
            String queryString = "SELECT VT.TENVT, VT.SOLUONGTON FROM VATTU AS VT";
            PreparedStatement statement = connectionHolder.getConnection().prepareStatement(queryString);
            ResultSet resultSet = statement.executeQuery();

            List<String> supplies = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();
            while (resultSet.next()) {
                supplies.add(resultSet.getString("TENVT"));
                quantities.add(resultSet.getInt("SOLUONGTON"));
            }

            result.setSupplies(supplies);
            Double total = (double) 0;
            for (Integer quantity: quantities) {
                total += quantity;
            }
            List<Double> percentages = new ArrayList<>();
            for (Integer quantity: quantities) {
                percentages.add(quantity / total * 100.0);
            }
            result.setPercentages(percentages);

            //--Close all connection.
            resultSet.close();
            statement.close();
        }
        catch (Exception exception) {
            logger.info("Error In 'calculateTotalImport' of HomePageRepository: " + exception);
        }
        return result;
    }

    public SupplyTrendDto calculateSuppliesTrend(HttpServletRequest request) {
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
        SupplyTrendDto result = new SupplyTrendDto();
        try {
            String queryString = "{call SP_SUPPLY_TREND()}";
            PreparedStatement statement = connectionHolder.getConnection().prepareStatement(queryString);
            ResultSet resultSet = statement.executeQuery();

            List<String> supplies = new ArrayList<>();
            List<Integer> totalImports = new ArrayList<>();
            List<Integer> totalExports = new ArrayList<>();
            while (resultSet.next()) {
                supplies.add(resultSet.getString("TENVT"));
                totalImports.add(resultSet.getInt("TONG_NHAP"));
                totalExports.add(resultSet.getInt("TONG_XUAT"));
            }

            result.setSupplies(supplies);
            result.setSupplyTotalImport(totalImports);
            result.setSupplyTotalExport(totalExports);

            //--Close all connection.
            resultSet.close();
            statement.close();
        }
        catch (Exception exception) {
            logger.info("Error In 'calculateTotalImport' of HomePageRepository: " + exception);
        }
        return result;
    }

    public TotalImportAndExportOfYearDto totalImportAndExportOfYear(HttpServletRequest request, int year) {
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
        TotalImportAndExportOfYearDto result = new TotalImportAndExportOfYearDto();
        try {
            String callSPStatement = "{call SP_REPORT_OF_PERCENTAGE_OF_IMPORT_AND_EXPORT(?, ?)}";
            PreparedStatement statement = connectionHolder.getConnection().prepareStatement(callSPStatement);
            statement.setDate(1,  Date.valueOf(year + "-01-01"));
            statement.setDate(2, Date.valueOf(year + "-12-31"));
            ResultSet resultSet = statement.executeQuery();

            Map<Integer, Integer> monthlyTotalImport = new HashMap<>();
            Map<Integer, Integer> monthlyTotalExport = new HashMap<>();
            while (resultSet.next()) {
                Date reportDate = resultSet.getDate("NGAY");
                int reportMonth = reportDate.getMonth();
                int importValue = resultSet.getInt("NHAP");
                int exportValue = resultSet.getInt("XUAT");

                // * Update monthly import map
                if (monthlyTotalImport.containsKey(reportMonth)) {
                    Integer currentTotalImport = monthlyTotalImport.get(reportMonth);
                    currentTotalImport += importValue;
                    monthlyTotalImport.put(reportMonth, currentTotalImport);
                }
                else {
                    monthlyTotalImport.put(reportMonth, importValue);
                }

                // * Update monthly export map
                if (monthlyTotalExport.containsKey(reportMonth)) {
                    Integer currentTotalExport = monthlyTotalExport.get(reportMonth);
                    currentTotalExport += exportValue;
                    monthlyTotalExport.put(reportMonth, currentTotalExport);
                }
                else {
                    monthlyTotalExport.put(reportMonth, exportValue);
                }
            }

            List<Integer> monthlyTotalImportList = IntStream.rangeClosed(1, 12)
                    .mapToObj(month -> monthlyTotalImport.getOrDefault(month, 0))
                    .toList();
            List<Integer> monthlyTotalExportList = IntStream.rangeClosed(1, 12)
                    .mapToObj(month -> monthlyTotalExport.getOrDefault(month, 0))
                    .toList();
            result.setYear(year);
            result.setMonthlyImport(monthlyTotalImportList);
            result.setMonthlyExport(monthlyTotalExportList);

            //--Close all connection.
            resultSet.close();
            statement.close();
        }
        catch (Exception exception) {
            logger.info("Error In 'totalImportAndExportOfYear' of HomePageRepository: " + exception);
        }
        return result;
    }
}
