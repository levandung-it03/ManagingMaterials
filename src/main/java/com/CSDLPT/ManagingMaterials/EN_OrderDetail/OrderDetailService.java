package com.CSDLPT.ManagingMaterials.EN_OrderDetail;

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
import java.util.NoSuchElementException;

public class OrderDetailService {

    @Service
    @RequiredArgsConstructor
    public static class BranchServices {
        private final StaticUtilMethods staticUtilMethods;
        private final FindingActionService findingActionService;
        private final OrderDetailRepository orderDetailRepository;

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
                throw new DuplicateKeyException("This Supply Id is already existing in Order-Detail-List in DB");

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

            orderDetailRepository.update(connectHolder, orderDetail);
            //--Close connection
            connectHolder.removeConnection();
        }

        public void deleteOrderDetail(
            String orderDetailId,
            String supplyId,
            HttpServletRequest request
        ) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            if (orderDetailRepository.findById(connectHolder, orderDetailId, supplyId).isEmpty())
                throw new NoSuchElementException("Order-Detail not found");

            orderDetailRepository.delete(connectHolder, orderDetailId, supplyId);
            //--Close connection
            connectHolder.removeConnection();
        }
    }

    @Service
    public static class CompanyServices {

    }

    @Service
    public static class UserServices {

    }
}
