package com.CSDLPT.ManagingMaterials.EN_SuppliesExportationDetail;

import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.EN_OrderDetail.dtos.ReqDtoDataForDetail;
import com.CSDLPT.ManagingMaterials.EN_SuppliesExportation.SuppliesExportationRepository;
import com.CSDLPT.ManagingMaterials.EN_Supply.SupplyRepository;
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
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class SuppliesExportationDetailService {
    @Service
    @RequiredArgsConstructor
    public static class AuthenticatedServices {
        private final StaticUtilMethods staticUtilMethods;
        private final FindingActionService findingActionService;
        private final SuppliesExportationDetailRepository suppliesExportationDetailRepository;
        private final SuppliesExportationRepository suppliesExportationRepository;
        private final SupplyRepository supplyRepository;

        public ModelAndView getManageSuppliesExportationDetailPage(HttpServletRequest request, Model model) {
            //--Prepare a modelAndView object to symbolize the whole page.
            final String suppliesExportationId = request.getParameter("suppliesExportationId");
            ModelAndView modelAndView = staticUtilMethods
                .customResponsiveModelView(request, model, "manage-supplies-exportation-detail");

            //--Check if there's a response SuppliesExportation to map into adding-form when an error occurred.
            SuppliesExportationDetail detail =
                (SuppliesExportationDetail) model.asMap().get("submittedSuppliesExportationDetail");
            if (detail != null) modelAndView.addObject("exportationDetail", detail);
            else
                //--Give the default value to suppliesExportationDetail.
                modelAndView.addObject("exportationDetail", SuppliesExportationDetail.builder()
                    .suppliesExportationId(suppliesExportationId)
                    .build());

            return modelAndView;
        }

        public ResDtoRetrievingData<SuppliesExportationDetail> findingSuppliesExportationDetail(
            HttpServletRequest request,
            ReqDtoRetrievingData<SuppliesExportationDetail> searchingObject
        ) throws SQLException, NoSuchFieldException {
            //--Preparing data to fetch.
            searchingObject.setObjectType(SuppliesExportationDetail.class);
            searchingObject.setSearchingTable("CTPX");
            searchingObject.setSearchingTableIdName("MAPX");
            searchingObject.setSortingCondition("ORDER BY MAPX ASC");

            StringBuilder condition = new StringBuilder();
            for (ReqDtoDataForDetail dataObject : searchingObject.getConditionObjectsList()) {
                //--Convert object name into field name in DB
                dataObject.setName(staticUtilMethods.columnNameStaticDictionary(dataObject.getName()).getFirst());
                //--Add more condition to condition variable
                condition.append(!condition.isEmpty() ? " AND " : "");
                condition.append(String.format("%s = '%s'", dataObject.getName(), dataObject.getValue()));
            }
            searchingObject.setMoreCondition(condition.toString());

            return findingActionService.findingDataAndServePaginationBarFormat(request, searchingObject);
        }

        public void addSuppliesExportationDetail(
            SuppliesExportationDetail exportationDetail,
            HttpServletRequest request
        ) throws Exception {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            ResDtoUserInfo currentUserInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");
            exportationDetail.trimAllFieldValues();

            //--Check if CTPX(MAPX-MAVT) is already existing.
            if (suppliesExportationDetailRepository.findById(connectHolder,
                exportationDetail.getSuppliesExportationId(), exportationDetail.getSupplyId()
            ).isPresent())
                throw new DuplicateKeyException("error_supply_01");

            //--Check if MAVT doesn't exist.
            if (!supplyRepository.isExistingSupplyBySupplyId(connectHolder,exportationDetail.getSupplyId()))
                throw new NoSuchElementException("error_supply_04");

            //--Check if MAPX is not existing, and not belong to employee who is logging-in.
            if (suppliesExportationRepository.findBySuppliesExportationIdAndEmployeeId(
                connectHolder, exportationDetail.getSuppliesExportationId(), currentUserInfo.getEmployeeId()
            ).isEmpty())
                throw new NoSuchElementException("error_suppliesExportation_04");

            int addRes = suppliesExportationDetailRepository.saveByStoredProc(connectHolder, exportationDetail);
            if (addRes == -2)   throw new SQLIntegrityConstraintViolationException("quantityInStock is not enough");
            if (addRes == -1)   throw new NoSuchElementException("error_entity_03");
            if (addRes == 0)    throw new SQLException("Something wrong in your application");

            //--Close connection
            connectHolder.removeConnection();
        }

        public void updateSuppliesExportationDetail(
            SuppliesExportationDetail exportationDetail,
            HttpServletRequest request
        ) throws Exception {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            ResDtoUserInfo currentUserInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");
            exportationDetail.trimAllFieldValues();

            //--Check if CTPX(MAPX-MAVT) is not existing.
            if (suppliesExportationDetailRepository.findById(
                connectHolder, exportationDetail.getSuppliesExportationId(), exportationDetail.getSupplyId()
            ).isEmpty())
                throw new NoSuchElementException("error_entity_03");

            //--Check if MAPX is not existing, and not belong to employee who is logging-in.
            if (suppliesExportationRepository.findBySuppliesExportationIdAndEmployeeId(
                connectHolder, exportationDetail.getSuppliesExportationId(), currentUserInfo.getEmployeeId()
            ).isEmpty())
                throw new NoSuchElementException("error_suppliesExportation_04");

            int updateRes = suppliesExportationDetailRepository.updateByStoredProc(connectHolder, exportationDetail);
            if (updateRes == -2)    throw new SQLIntegrityConstraintViolationException("quantityInStock is not enough");
            if (updateRes == -1)    throw new NoSuchElementException("error_entity_03");
            if (updateRes == 0)     throw new SQLException("Something wrong in your application");

            suppliesExportationDetailRepository.update(connectHolder, exportationDetail);
            //--Close connection
            connectHolder.removeConnection();
        }
    }

    @Service
    public static class BranchServices {

    }
    @Service
    public static class CompanyServices {

    }

    @Service
    public static class UserServices {

    }
}
