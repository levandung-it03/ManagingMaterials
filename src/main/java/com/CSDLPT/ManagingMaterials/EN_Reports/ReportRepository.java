package com.CSDLPT.ManagingMaterials.EN_Reports;

import com.CSDLPT.ManagingMaterials.EN_Reports.dto.ImportAndExportStatistic;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportRepository {
    private final Logger logger;

    public List<ImportAndExportStatistic> findImportAndExportStatistic(
            HttpServletRequest request, Date fromDate, Date toDate) {
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
        List<ImportAndExportStatistic> result = new ArrayList<>();
        try {
            String callSPStatement = "{call SP_REPORT_OF_PERCENTAGE_OF_IMPORT_AND_EXPORT(?, ?)}";
            PreparedStatement statement = connectionHolder.getConnection().prepareStatement(callSPStatement);
            statement.setDate(1, fromDate);
            statement.setDate(2, toDate);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Date reportDate = resultSet.getDate("NGAY");
                int importValue = resultSet.getInt("NHAP");
                double importPercentage = resultSet.getDouble("TI_LE_NHAP");
                int exportValue = resultSet.getInt("XUAT");
                double exportPercentage = resultSet.getDouble("TI_LE_XUAT");

                ImportAndExportStatistic data = ImportAndExportStatistic.builder()
                        .date(reportDate)
                        .supplyImport(importValue)
                        .importPercentage(importPercentage)
                        .supplyExport(exportValue)
                        .exportPercentage(exportPercentage)
                        .build();
                result.add(data);
            }

            //--Close all connection.
            resultSet.close();
            statement.close();
        }
        catch (Exception exception) {
            logger.info("Error In 'findImportAndExportStatistic' of ReportRepository: " + exception);
        }
        return result;
    }
}
