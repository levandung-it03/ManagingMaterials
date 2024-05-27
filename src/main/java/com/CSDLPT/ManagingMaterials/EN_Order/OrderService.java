package com.CSDLPT.ManagingMaterials.EN_Order;

import com.CSDLPT.ManagingMaterials.EN_Order.dtos.ResDtoOrderWithImportantInfo;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.FindingActionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Or;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.Date;

public class OrderService {

    @Service
    @RequiredArgsConstructor
    public static class BranchServices {
        private final StaticUtilMethods staticUtilMethods;
        private final FindingActionService findingActionService;
        public ModelAndView getManageOrderPage(HttpServletRequest request, Model model) {
            //--Prepare common-components of ModelAndView if we need.
            ModelAndView modelAndView = staticUtilMethods
                .customResponsiveModelView(request, model, "manage-order");
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

        public ResDtoRetrievingData<ResDtoOrderWithImportantInfo> findOrderToMakeSuppliesImportation(
            HttpServletRequest request,
            ReqDtoRetrievingData<ResDtoOrderWithImportantInfo> searchingObject
        ) throws SQLException, NoSuchFieldException {
            //--Preparing data to fetch.
            searchingObject.setObjectType(ResDtoOrderWithImportantInfo.class);
            searchingObject.setSearchingTable("DatHang");
            searchingObject.setSearchingTableIdName("MasoDDH");
            searchingObject.setSortingCondition("ORDER BY NGAY ASC");
            searchingObject.setJoiningCondition(
                "INNER JOIN (SELECT MANV, HO, TEN FROM NHANVIEN) AS EmployeeFromFk ON DatHang.MANV = EmployeeFromFk.MANV "
                + "INNER JOIN (SELECT MAKHO, TENKHO FROM KHO) AS WarehouseFromFk ON DatHang.MAKHO = WarehouseFromFk.MAKHO"
            );

            return findingActionService.findingDataAndServePaginationBarFormat(request, searchingObject);
        }
    }

    @Service
    public static class CompanyServices {

    }

    @Service
    public static class UserServices {

    }

}
