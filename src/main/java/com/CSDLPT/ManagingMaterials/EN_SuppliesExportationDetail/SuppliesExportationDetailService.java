package com.CSDLPT.ManagingMaterials.EN_SuppliesExportationDetail;

import com.CSDLPT.ManagingMaterials.EN_OrderDetail.dtos.ReqDtoDataForDetail;
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
import java.util.NoSuchElementException;

public class SuppliesExportationDetailService {
    @Service
    @RequiredArgsConstructor
    public static class BranchServices {
        private final StaticUtilMethods staticUtilMethods;
        private final FindingActionService findingActionService;
        private final SuppliesExportationDetailRepository suppliesExportationDetailRepository;

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
        ) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            exportationDetail.trimAllFieldValues();

            if (suppliesExportationDetailRepository.findById(connectHolder,
                exportationDetail.getSuppliesExportationId(), exportationDetail.getSupplyId()
            ).isPresent())
                throw new DuplicateKeyException("This Supply Id is already existing in Sup-Export-List in DB");

            suppliesExportationDetailRepository.save(connectHolder, exportationDetail);
            //--Close connection
            connectHolder.removeConnection();
        }

        public void updateSuppliesExportationDetail(
            SuppliesExportationDetail exportationDetail,
            HttpServletRequest request
        ) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            exportationDetail.trimAllFieldValues();

            suppliesExportationDetailRepository.update(connectHolder, exportationDetail);
            //--Close connection
            connectHolder.removeConnection();
        }

        public void deleteSuppliesExportationDetail(
            String exportationDetailId,
            String supplyId,
            HttpServletRequest request
        ) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            if (suppliesExportationDetailRepository.findById(connectHolder, exportationDetailId, supplyId).isEmpty())
                throw new NoSuchElementException("Supplies-Exportation-Detail not found");

            suppliesExportationDetailRepository.delete(connectHolder, exportationDetailId, supplyId);
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
