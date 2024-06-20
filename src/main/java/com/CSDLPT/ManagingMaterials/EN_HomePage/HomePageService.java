package com.CSDLPT.ManagingMaterials.EN_HomePage;

import com.CSDLPT.ManagingMaterials.EN_HomePage.dto.InventoryPercentageDto;
import com.CSDLPT.ManagingMaterials.EN_HomePage.dto.SupplyTrendDto;
import com.CSDLPT.ManagingMaterials.EN_HomePage.dto.TotalImportAndExportOfYearDto;
import com.CSDLPT.ManagingMaterials.EN_Supply.Supply;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.FindingActionService;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomePageService {
    private final FindingActionService findingActionService;

    private final HomePageRepository homePageRepository;

    public Integer countNumberOfSupply(HttpServletRequest request) {
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        ReqDtoRetrievingData<Supply> searchingObject = new ReqDtoRetrievingData<>();
        searchingObject.setSearchingTable("VATTU");
        searchingObject.setSearchingTableIdName("MAVT");
        searchingObject.setSearchingValue("1");

        return findingActionService.countAllByCondition(connectionHolder, searchingObject, "WHERE 1 = ?");
    }

    public Integer getTotalRevenueOfBranch(HttpServletRequest request) {
        Integer totalImport = this.homePageRepository.calculateTotalImport(request);
        Integer totalExport = this.homePageRepository.calculateTotalExport(request);
        return totalExport - totalImport;
    }

    public Integer countNumberOfEmployee(HttpServletRequest request) {
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        ReqDtoRetrievingData<Supply> searchingObject = new ReqDtoRetrievingData<>();
        searchingObject.setSearchingTable("NHANVIEN");
        searchingObject.setSearchingTableIdName("MANV");
        searchingObject.setSearchingValue("0");
        String searchingCondition = "TRANGTHAIXOA = ?";

        return findingActionService.countAllByCondition(connectionHolder, searchingObject, searchingCondition);
    }

    public Integer countNumberOfExportation(HttpServletRequest request) {
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        ReqDtoRetrievingData<Supply> searchingObject = new ReqDtoRetrievingData<>();
        searchingObject.setSearchingTable("PHIEUXUAT");
        searchingObject.setSearchingTableIdName("MAPX");
        searchingObject.setSearchingValue("1");

        return findingActionService.countAllByCondition(connectionHolder, searchingObject, "WHERE 1 = ?");
    }

    public Integer countNumberOfImportation(HttpServletRequest request) {
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        ReqDtoRetrievingData<Supply> searchingObject = new ReqDtoRetrievingData<>();
        searchingObject.setSearchingTable("PHIEUNHAP");
        searchingObject.setSearchingTableIdName("MAPN");
        searchingObject.setSearchingValue("1");

        return findingActionService.countAllByCondition(connectionHolder, searchingObject, "WHERE 1 = ?");
    }

    public Integer countNumberOfOrder(HttpServletRequest request) {
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        ReqDtoRetrievingData<Supply> searchingObject = new ReqDtoRetrievingData<>();
        searchingObject.setSearchingTable("DATHANG");
        searchingObject.setSearchingTableIdName("MADDH");
        searchingObject.setSearchingValue("1");

        return findingActionService.countAllByCondition(connectionHolder, searchingObject, "WHERE 1 = ?");
    }

    public Integer countNumberOfOrderWithoutImportation(HttpServletRequest request) {
        return this.countNumberOfOrder(request) - this.countNumberOfImportation(request);
    }

    public TotalImportAndExportOfYearDto getMonthlyTotalImportAndExportOfYear(HttpServletRequest request, int year) {
        return this.homePageRepository.totalImportAndExportOfYear(request, year);
    }

    public SupplyTrendDto getSupplyTrend(HttpServletRequest request) {
        return this.homePageRepository.calculateSuppliesTrend(request);
    }

    public InventoryPercentageDto getInventoryPercentages(HttpServletRequest request) {
        return this.homePageRepository.calculateInventoryPercentage(request);
    }
}
