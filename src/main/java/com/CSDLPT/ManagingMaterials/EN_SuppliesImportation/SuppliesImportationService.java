package com.CSDLPT.ManagingMaterials.EN_SuppliesImportation;

import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.EN_Order.OrderRepository;
import com.CSDLPT.ManagingMaterials.EN_SuppliesImportation.dtos.ReqDtoAddSuppliesImportation;
import com.CSDLPT.ManagingMaterials.EN_Warehouse.WarehouseRepository;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.FindingActionService;
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
import java.util.NoSuchElementException;

public class SuppliesImportationService {
    @Service
    @RequiredArgsConstructor
    public static class BranchServices {
        private final StaticUtilMethods staticUtilMethods;
        private final FindingActionService findingActionService;
        private final SuppliesImportationRepository suppliesImportationRepository;
        private final WarehouseRepository warehouseRepository;
        private final OrderRepository orderRepository;

        public ModelAndView getManageSuppliesImportationPage(HttpServletRequest request, Model model) {
            //--Prepare a modelAndView object to symbolize the whole page.
            ModelAndView modelAndView = staticUtilMethods
                .customResponsiveModelView(request, model, "manage-supplies-importation");

            //--Check if there's a response SuppliesImportation to map into adding-form when an error occurred.
            SuppliesImportation importation = (SuppliesImportation) model.asMap().get("submittedSuppliesImportation");
            if (importation != null) modelAndView.addObject("suppliesImportation", importation);

            return modelAndView;
        }

        public ResDtoRetrievingData<SuppliesImportation> findSuppliesImportation(
            HttpServletRequest request,
            ReqDtoRetrievingData<SuppliesImportation> searchingObject
        ) throws SQLException, NoSuchFieldException {
            //--Preparing data to fetch.
            searchingObject.setObjectType(SuppliesImportation.class);
            searchingObject.setSearchingTable("PhieuNhap");
            searchingObject.setSearchingTableIdName("MAPN");
            searchingObject.setSortingCondition("ORDER BY MAPN ASC");

            return findingActionService.findingDataAndServePaginationBarFormat(request, searchingObject);
        }

        public void addSuppliesImportation(ReqDtoAddSuppliesImportation importation, HttpServletRequest request) {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            importation.trimAllFieldValues();

            if (suppliesImportationRepository.isExistingSuppliesImportationBySuppliesImportationId(
                connectHolder, importation.getSuppliesImportationId()
            ))
                throw new DuplicateKeyException("Supplies Importation Id is already existing");
            
            if (!warehouseRepository.isExistingWarehouseByWarehouseId(connectHolder, importation.getWarehouseId()))
                throw new NoSuchElementException("error_warehouse_02");

            if (!orderRepository.isExistingOrderByOrderId(connectHolder, importation.getOrderId()))
                throw new NoSuchElementException("error_order_01");

            ResDtoUserInfo currentUserInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");
            //--May throw SQLException if id is already existing.
            suppliesImportationRepository.save(connectHolder, SuppliesImportation.builder()
                .suppliesImportationId(importation.getSuppliesImportationId())
                .warehouseId(importation.getWarehouseId())
                .orderId(importation.getOrderId())
                .employeeId(currentUserInfo.getEmployeeId())
                .createdDate(new Date())
                .build()
            );
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
