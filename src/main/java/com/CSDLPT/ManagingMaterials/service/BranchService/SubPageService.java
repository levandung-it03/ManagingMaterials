package com.CSDLPT.ManagingMaterials.service.BranchService;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.model.OrderDetail;
import com.CSDLPT.ManagingMaterials.service.GeneralService.FindingActionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@Service
@RequiredArgsConstructor
public class SubPageService {
    private final StaticUtilMethods staticUtilMethods;

    public ModelAndView getManageOrderDetailPage(HttpServletRequest request, Model model) {
        //--Prepare common-components of ModelAndView if we need.
        final String orderId = request.getParameter("orderId");
        ModelAndView modelAndView = staticUtilMethods
                .customResponseModelView(request, model.asMap(), "manage-order-detail");

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
