package com.CSDLPT.ManagingMaterials.EN_OrderDetail;

import com.CSDLPT.ManagingMaterials.EN_SuppliesImportation.SuppliesImportation;
import com.CSDLPT.ManagingMaterials.EN_SuppliesImportation.SuppliesImportationRepository;
import com.CSDLPT.ManagingMaterials.EN_SuppliesImportationDetail.SuppliesImportationDetail;
import com.CSDLPT.ManagingMaterials.EN_SuppliesImportationDetail.SuppliesImportationDetailRepository;
import com.CSDLPT.ManagingMaterials.EN_Supply.SupplyRepository;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.EN_OrderDetail.dtos.ReqDtoDataForDetail;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.FindingActionService;
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

public class OrderDetailService {

    @Service
    @RequiredArgsConstructor
    public static class AuthenticatedServices {
        private final StaticUtilMethods staticUtilMethods;
        private final FindingActionService findingActionService;
        private final OrderDetailRepository orderDetailRepository;
        private final SupplyRepository supplyRepository;
        private final SuppliesImportationRepository suppliesImportationRepository;
        private final SuppliesImportationDetailRepository suppliesImportationDetailRepository;

        public ModelAndView getManageOrderDetailPage(HttpServletRequest request, Model model) {
            //--Prepare a modelAndView object to symbolize the whole page.
            final String orderId = request.getParameter("orderId");
            ModelAndView modelAndView = staticUtilMethods
                .customResponsiveModelView(request, model, "manage-order-detail");

            //--Check if there's a response Order to map into adding-form when an error occurred.
            OrderDetail detail =
                (OrderDetail) model.asMap().get("submittedOrderDetail");
            if (detail != null) modelAndView.addObject("orderDetail", detail);
            else
                //--Give the default value to orderDetail.
                modelAndView.addObject("orderDetail", OrderDetail.builder()
                    .orderId(orderId)
                    .build());

            return modelAndView;
        }

        public ResDtoRetrievingData<OrderDetail> findingOrderDetail(
            HttpServletRequest request,
            ReqDtoRetrievingData<OrderDetail> searchingObject
        ) throws SQLException, NoSuchFieldException {
            //--Preparing data to fetch.
            searchingObject.setObjectType(OrderDetail.class);
            searchingObject.setSearchingTable("CTDDH");
            searchingObject.setSearchingTableIdName("MasoDDH");
            searchingObject.setSortingCondition("ORDER BY MasoDDH ASC");

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

        public void addOrderDetail(
            OrderDetail orderDetail,
            HttpServletRequest request
        ) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            orderDetail.trimAllFieldValues();

            if (orderDetailRepository.findById(connectHolder,
                orderDetail.getOrderId(), orderDetail.getSupplyId()
            ).isPresent())
                throw new DuplicateKeyException("error_supply_01");

            if (!supplyRepository.isExistingSupplyBySupplyId(connectHolder,orderDetail.getSupplyId()))
                throw new NoSuchElementException("error_supply_04");

            orderDetailRepository.save(connectHolder, orderDetail);
            //--Close connection
            connectHolder.removeConnection();
        }

        public void updateOrderDetail(
            OrderDetail orderDetail,
            HttpServletRequest request
        ) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            orderDetail.trimAllFieldValues();

            //--Check if order already has importation and get importation id
            Optional<SuppliesImportation> importation = suppliesImportationRepository
                .findByOrderId(connectHolder, orderDetail.getOrderId());
            if (importation.isPresent()){
                //--Check if order detail has corresponding importation detail
                Optional<SuppliesImportationDetail> importationDetail = suppliesImportationDetailRepository
                    .findById(connectHolder, importation.get().getSuppliesImportationId(), orderDetail.getSupplyId());
                if (importationDetail.isPresent()){
                    //--If quantity in order detail is smaller than in importation then throw exception
                    if (orderDetail.getSuppliesQuantity() < importationDetail.get().getSuppliesQuantity())
                        throw new SQLIntegrityConstraintViolationException("New suppliesQuantity value is invalid");
                }
            }


            orderDetailRepository.update(connectHolder, orderDetail);
            //--Close connection
            connectHolder.removeConnection();
        }

        public void deleteOrderDetail(
            String orderDetailId,
            HttpServletRequest request
        ) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            //--Get orderId and supplyId form orderDetailId
            String orderId = orderDetailId.split("-")[0];
            String supplyId = orderDetailId.split("-")[1];

            if (orderDetailRepository.findById(connectHolder, orderId, supplyId).isEmpty())
                throw new NoSuchElementException("Order-Detail not found");

            //--Check if order already has importation and get importation id
            Optional<SuppliesImportation> importation = suppliesImportationRepository
                .findByOrderId(connectHolder, orderId);
            if (importation.isPresent()){
                //--Check if order detail has corresponding importation detail
                if (suppliesImportationDetailRepository.findById(connectHolder, importation.get()
                    .getSuppliesImportationId(), supplyId).isPresent())
                    throw new SQLIntegrityConstraintViolationException("Order Detail already had corresponding Importation Detail");
            }

            orderDetailRepository.delete(connectHolder, orderId, supplyId);
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
