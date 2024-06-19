package com.CSDLPT.ManagingMaterials.EN_HomePage;

import com.CSDLPT.ManagingMaterials.EN_HomePage.dto.InventoryPercentageDto;
import com.CSDLPT.ManagingMaterials.EN_HomePage.dto.SupplyTrendDto;
import com.CSDLPT.ManagingMaterials.EN_HomePage.dto.TotalImportAndExportOfYearDto;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class HomePageController {
    private final HomePageService homePageService;
    private final StaticUtilMethods staticUtilMethods;

    @GetMapping("/home")
    public ModelAndView homePageController(HttpServletRequest request, Model model) {
        ModelAndView modelAndView = staticUtilMethods.customResponsiveModelView(request, model, "home");

        int numberOfSupply = this.homePageService.countNumberOfSupply(request);
        int totalRevenue = this.homePageService.getTotalRevenueOfBranch(request);
        int numberOfEmployee = this.homePageService.countNumberOfEmployee(request);
        int numberOfExportation = this.homePageService.countNumberOfExportation(request);
        int numberOfOrderWithoutImportation = this.homePageService.countNumberOfOrderWithoutImportation(request);

        modelAndView.addObject("numberOfSupply", numberOfSupply);
        modelAndView.addObject("totalRevenue", totalRevenue);
        modelAndView.addObject("numberOfEmployee", numberOfEmployee);
        modelAndView.addObject("numberOfExportation", numberOfExportation);
        modelAndView.addObject("numberOfOrderWithoutImportation", numberOfOrderWithoutImportation);

        return modelAndView;
    }

    @PostMapping({"${url.post.branch.prefix.v1}/total-monthly-import",
        "${url.post.company.prefix.v1}/total-monthly-import",
        "${url.post.user.prefix.v1}/total-monthly-import"})
    public ResponseEntity<TotalImportAndExportOfYearDto> findMonthlyTotalImportAndExport(HttpServletRequest request) {
        int currentYear = LocalDate.now().getYear();
        TotalImportAndExportOfYearDto totalImportAndExportOfYearDto =
                this.homePageService.getMonthlyTotalImportAndExportOfYear(request, currentYear);
        return ResponseEntity.ok(totalImportAndExportOfYearDto);
    }

    @PostMapping({"${url.post.branch.prefix.v1}/inventory",
        "${url.post.company.prefix.v1}/inventory",
        "${url.post.user.prefix.v1}/inventory"})
    public ResponseEntity<InventoryPercentageDto> findInventoryPercentage(HttpServletRequest request) {
        InventoryPercentageDto inventoryPercentageDto = this.homePageService.getInventoryPercentages(request);
        return ResponseEntity.ok(inventoryPercentageDto);
    }

    @PostMapping({"${url.post.branch.prefix.v1}/supply-trend",
            "${url.post.company.prefix.v1}/supply-trend",
            "${url.post.user.prefix.v1}/supply-trend"})
    public ResponseEntity<SupplyTrendDto> findSupplyTrend(HttpServletRequest request) {
        SupplyTrendDto supplyTrendDto = this.homePageService.getSupplyTrend(request);
        return ResponseEntity.ok(supplyTrendDto);
    }
}
