package com.CSDLPT.ManagingMaterials.service.BranchService;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.dto.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.dto.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.dto.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.model.Order;
import com.CSDLPT.ManagingMaterials.repository.OrderRepository;
import com.CSDLPT.ManagingMaterials.service.GeneralService.FindingActionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final StaticUtilMethods staticUtilMethods;
    private final OrderRepository orderRepository;
    private final FindingActionService findingActionService;

    public ModelAndView getManageOrderPage(HttpServletRequest request, Model model) {
        //--Prepare common-components of ModelAndView if we need.
        ModelAndView modelAndView = staticUtilMethods
                .customResponseModelView(request, model.asMap(), "manage-order");
        //--If there's an error when handle data with DB, take the submitted-order-info and give it back to this page
        Order order = (Order) model.asMap().get("submittedOrder");
        if (order != null) modelAndView.addObject("order", order);
        else {
            //--Get Current_Login_User from Session.
            ResDtoUserInfo userInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");

            //--Give the default value to order.
            modelAndView.addObject("order", Order.builder()
                    .employeeId(Integer.valueOf(userInfo.getEmployeeId()))
                    .createdDate(new Date())
                    .build());
        }
        return modelAndView;
    }

    public ResDtoRetrievingData<Order> findOrder(
            HttpServletRequest request,
            ReqDtoRetrievingData<Order> searchingObject
    ) throws SQLException, NoSuchFieldException {
        //--Preparing data to fetch.
        searchingObject.setObjectType(Order.class);
        searchingObject.setSearchingTable("DatHang");
        searchingObject.setSearchingTableIdName("MasoDDH");
        searchingObject.setSortingCondition("ORDER BY MasoDDH ASC");

        return findingActionService.findingDataAndServePaginationBarFormat(request, searchingObject);
    }
}
