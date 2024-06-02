package com.CSDLPT.ManagingMaterials.EN_SuppliesExportation;

import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.EN_SuppliesExportation.dtos.ReqDtoSuppliesExportation;
import com.CSDLPT.ManagingMaterials.EN_SuppliesExportation.dtos.ResDtoExportationWithImportationInfo;
import com.CSDLPT.ManagingMaterials.EN_Warehouse.WarehouseRepository;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.FindingActionService;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.InnerJoinObject;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class SuppliesExportationService {
    @Service
    @RequiredArgsConstructor
    public static class BranchServices {
        private final StaticUtilMethods staticUtilMethods;
        private final FindingActionService findingActionService;
        private final SuppliesExportationRepository suppliesExportationRepository;
        private final WarehouseRepository warehouseRepository;

        public ModelAndView getManageSuppliesExportationPage(HttpServletRequest request, Model model) {
            //--Prepare a modelAndView object to symbolize the whole page.
            ModelAndView modelAndView = staticUtilMethods
                .customResponsiveModelView(request, model, "manage-supplies-exportation");

            //--Check if there's a response SuppliesExportation to map into adding-form when an error occurred.
            ReqDtoSuppliesExportation exportation = (ReqDtoSuppliesExportation) model.asMap().get("submittedSuppliesExportation");
            if (exportation != null) modelAndView.addObject("suppliesExportation", exportation);

            return modelAndView;
        }

        public ResDtoRetrievingData<ResDtoExportationWithImportationInfo> findingSuppliesExportation(
            HttpServletRequest request,
            ReqDtoRetrievingData<ResDtoExportationWithImportationInfo> searchingObject
        ) throws SQLException, NoSuchFieldException {
            //--Preparing data to fetch.
            searchingObject.setObjectType(ResDtoExportationWithImportationInfo.class);
            searchingObject.setSearchingTable("PhieuXuat");
            searchingObject.setSearchingTableIdName("MAPX");
            searchingObject.setSortingCondition("ORDER BY MAPX ASC");
            searchingObject.setJoiningCondition(InnerJoinObject.mergeQuery(List.of(
                InnerJoinObject.builder().left("PhieuXuat").right("NHANVIEN").fields("MANV, HO, TEN").bridge("MANV").build(),
                InnerJoinObject.builder().left("PhieuXuat").right("KHO").fields("MAKHO, TENKHO").bridge("MAKHO").build()
            )));

            return findingActionService.findingDataAndServePaginationBarFormat(request, searchingObject);
        }

        public void addSuppliesExportation(
            ReqDtoSuppliesExportation exportation,
            HttpServletRequest request
        ) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            exportation.trimAllFieldValues();

            if (suppliesExportationRepository
                .isExistingSuppliesExportationBySuppliesExportationId(connectHolder, exportation.getSuppliesExportationId()))
                throw new DuplicateKeyException("error_suppliesExportation_01");

            if (!warehouseRepository.isExistingWarehouseByWarehouseId(connectHolder, exportation.getWarehouseIdAsFk()))
                throw new NoSuchElementException("error_warehouse_02");

            ResDtoUserInfo currentUserInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");
            //--May throw SQLException if id is already existing.
            suppliesExportationRepository.save(connectHolder, SuppliesExportation.builder()
                .suppliesExportationId(exportation.getSuppliesExportationId())
                .customerFullName(exportation.getCustomerFullName())
                .warehouseId(exportation.getWarehouseIdAsFk())
                .employeeId(currentUserInfo.getEmployeeId())
                .createdDate(new Date())
                .build()
            );

            //--Close connection
            connectHolder.removeConnection();
        }
        public void updateSuppliesExportation(
            ReqDtoSuppliesExportation exportation,
            HttpServletRequest request
        ) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            exportation.trimAllFieldValues();

            if (suppliesExportationRepository.findById(connectHolder, exportation.getSuppliesExportationId()).isEmpty())
                throw new NoSuchElementException("error_suppliesExportation_02");

            if (!warehouseRepository.isExistingWarehouseByWarehouseId(connectHolder, exportation.getWarehouseIdAsFk()))
                throw new NoSuchElementException("error_warehouse_02");

            //--May throw SQLException if id is already existing.
            suppliesExportationRepository.updateById(connectHolder, SuppliesExportation.builder()
                .suppliesExportationId(exportation.getSuppliesExportationId())
                .customerFullName(exportation.getCustomerFullName())
                .warehouseId(exportation.getWarehouseIdAsFk())
                .build()
            );

            //--Close connection
            connectHolder.removeConnection();
        }

        public void deleteSuppliesExportation(String exportationId, HttpServletRequest request) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            if (suppliesExportationRepository.findById(connectHolder, exportationId).isEmpty())
                throw new NoSuchElementException("Supplies Exportation Id is invalid");

            suppliesExportationRepository.deleteById(connectHolder, exportationId);

            //--Close connection
            connectHolder.removeConnection();
        }
    }

    @Service
    @RequiredArgsConstructor
    public static class CompanyServices {

    }

    @Service
    @RequiredArgsConstructor
    public static class UserServices {

    }
}