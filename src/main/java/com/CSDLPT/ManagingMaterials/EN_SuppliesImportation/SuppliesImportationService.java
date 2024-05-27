package com.CSDLPT.ManagingMaterials.EN_SuppliesImportation;

import com.CSDLPT.ManagingMaterials.EN_Warehouse.Warehouse;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.FindingActionService;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

public class SuppliesImportationService {

    @Service
    @RequiredArgsConstructor
    public static class BranchServices {
        private final StaticUtilMethods staticUtilMethods;
        private final FindingActionService findingActionService;

        public ModelAndView getManageSuppliesImportationPage(HttpServletRequest request, Model model) {
            //--Prepare a modelAndView object to symbolize the whole page.
            ModelAndView modelAndView = staticUtilMethods
                .customResponsiveModelView(request, model, "manage-supplies-importation");

            //--Check if there's a response SuppliesImportation to map into adding-form when an error occurred.
            SuppliesImportation importation = (SuppliesImportation) model.asMap().get("submittedSuppliesImportation");
            if (importation != null)   modelAndView.addObject("suppliesImportation", importation);

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
