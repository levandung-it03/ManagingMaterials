package com.CSDLPT.ManagingMaterials.EN_SuppliesImportationDetail;

import com.CSDLPT.ManagingMaterials.EN_Order.OrderRepository;
import com.CSDLPT.ManagingMaterials.EN_OrderDetail.dtos.ReqDtoDataForDetail;
import com.CSDLPT.ManagingMaterials.EN_SuppliesImportation.SuppliesImportationRepository;
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

public class SuppliesImportationDetailService {

    @Service
    @RequiredArgsConstructor
    public static class BranchServices {
        private final StaticUtilMethods staticUtilMethods;
        private final FindingActionService findingActionService;
        private final SuppliesImportationDetailRepository suppliesImportationDetailRepository;
        private final SuppliesImportationRepository suppliesImportationRepository;

        public ModelAndView getManageSuppliesImportationDetailPage(HttpServletRequest request, Model model) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            //--Prepare a modelAndView object to symbolize the whole page.
            final String suppliesImportationId = request.getParameter("suppliesImportationId");
            ModelAndView modelAndView = staticUtilMethods
                .customResponsiveModelView(request, model, "manage-supplies-importation-detail");

            //--Check if there's a response SuppliesImportation to map into adding-form when an error occurred.
            SuppliesImportationDetail detail =
                (SuppliesImportationDetail) model.asMap().get("submittedSuppliesImportationDetail");
            if (detail != null) modelAndView.addObject("importationDetail", detail);
            else {
                //--Give the default value to suppliesImportationDetail.
                modelAndView.addObject("importationDetail", SuppliesImportationDetail.builder()
                    .suppliesImportationId(suppliesImportationId)
                    .build());
                modelAndView.addObject("orderId", suppliesImportationRepository
                    .findById(connectHolder, suppliesImportationId)
                    .orElseThrow(() -> new NoSuchElementException("suppliesImportationId not found"))
                    .getOrderId());
            }

            connectHolder.removeConnection();
            return modelAndView;
        }

        public ResDtoRetrievingData<SuppliesImportationDetail> findingSuppliesImportationsDetail(
            HttpServletRequest request,
            ReqDtoRetrievingData<SuppliesImportationDetail> searchingObject
        ) throws SQLException, NoSuchFieldException {
            //--Preparing data to fetch.
            searchingObject.setObjectType(SuppliesImportationDetail.class);
            searchingObject.setSearchingTable("CTPN");
            searchingObject.setSearchingTableIdName("MAPN");
            searchingObject.setSortingCondition("ORDER BY MAPN ASC");

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

        public void addSuppliesImportationDetail(
            SuppliesImportationDetail importationDetail,
            HttpServletRequest request
        ) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            importationDetail.trimAllFieldValues();

            if (suppliesImportationDetailRepository.findById(connectHolder,
                importationDetail.getSuppliesImportationId(), importationDetail.getSupplyId()
            ).isPresent())
                throw new DuplicateKeyException("This Supply Id is already existing in Sup-Import-List in DB");

            int addRes = suppliesImportationDetailRepository.saveByStoredProc(connectHolder, importationDetail);
            if (addRes == -1)   throw new NoSuchElementException("SupplyId is invalid");
            if (addRes == 0)    throw new SQLException("Something wrong in your application");

            //--Close connection
            connectHolder.removeConnection();
        }

        public void updateSuppliesImportationDetail(
            SuppliesImportationDetail importationDetail,
            HttpServletRequest request
        ) throws Exception {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            importationDetail.trimAllFieldValues();

            //--Check if importation detail is existed
            Optional<SuppliesImportationDetail> currentImportDetail = suppliesImportationDetailRepository
                .findById(connectHolder, importationDetail.getSuppliesImportationId(), importationDetail.getSupplyId());
            if (currentImportDetail.isEmpty())
                throw new NoSuchElementException("Supplies-Importation-Detail not found");

            int updateRes = suppliesImportationDetailRepository.updateByStoredProc(connectHolder, importationDetail);
            if (updateRes == 0)     throw new SQLException("Some thing wrong with application");
            if (updateRes == -1)    throw new NoSuchElementException("SupplyId not found");
            if (updateRes == -2)    throw new SQLIntegrityConstraintViolationException("New suppliesQuantity value is invalid");

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
