package com.CSDLPT.ManagingMaterials.EN_Reports;

import com.CSDLPT.ManagingMaterials.EN_Reports.dto.ImportAndExportStatistic;
import com.CSDLPT.ManagingMaterials.EN_Reports.dto.ImportAndExportStatisticDto;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final StaticUtilMethods staticUtilMethods;

    @GetMapping("/report/service/v1/branch/import-and-export-statistic")
    public ModelAndView renderImportAndExportStatistic(HttpServletRequest request, Model model) {
        return staticUtilMethods.customResponsiveModelView(request, model, "home");
    }

    @PostMapping("/service/v1/branch/import-and-export-statistic")
    public ResponseEntity<ResDtoRetrievingData<ImportAndExportStatistic>> findImportAndExportStatistic(
            @RequestBody ImportAndExportStatisticDto payload,
            HttpServletRequest request) {
        return ResponseEntity.ok(this.reportService.findImportAndExportStatistic(request, payload.getStartingDate(),
                payload.getEndingDate()));
    }

}
