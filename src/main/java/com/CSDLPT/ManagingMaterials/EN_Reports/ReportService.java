package com.CSDLPT.ManagingMaterials.EN_Reports;

import com.CSDLPT.ManagingMaterials.EN_Reports.dto.ImportAndExportStatistic;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public ResDtoRetrievingData<ImportAndExportStatistic> findImportAndExportStatistic(
            HttpServletRequest request,
            java.util.Date fromDate, java.util.Date toDate) {
        ResDtoRetrievingData<ImportAndExportStatistic> result = new ResDtoRetrievingData<>();
        result.setResultDataSet(this.reportRepository.findImportAndExportStatistic(
                request, new Date(fromDate.getTime()), new Date(toDate.getTime())));
        return result;
    }
}
