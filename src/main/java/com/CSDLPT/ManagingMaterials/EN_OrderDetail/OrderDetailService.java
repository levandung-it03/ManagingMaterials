package com.CSDLPT.ManagingMaterials.EN_OrderDetail;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.EN_OrderDetail.dtos.ReqDtoDataForDetail;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.FindingActionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

public class OrderDetailService {

    @Service
    @RequiredArgsConstructor
    public static class BranchServices {
        private final StaticUtilMethods staticUtilMethods;
        private final FindingActionService findingActionService;

        public ResDtoRetrievingData<OrderDetail> findOrderDetail(
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

        public ModelAndView getManageOrderDetailPage(HttpServletRequest request, Model model) {
            //--Prepare common-components of ModelAndView if we need.
            final String orderId = request.getParameter("orderId");
            ModelAndView modelAndView = staticUtilMethods
                .customResponsiveModelView(request, model, "manage-order-detail");

            //--If there's an error when handle data with DB, take the submitted-order-info and give it back to this page
            OrderDetail orderDetail = (OrderDetail) model.asMap().get("submittedOrderDetail");
            if (orderDetail != null) modelAndView.addObject("orderDetail", orderDetail);
            else {
                //--Give the default value to orderDetail.
                modelAndView.addObject("orderDetail", OrderDetail.builder()
                    .orderId(orderId)
                    .build());
            }
            return modelAndView;
        }
    }

    @Service
    public static class CompanyServices {

    }

    @Service
    public static class UserServices {

    }
}
