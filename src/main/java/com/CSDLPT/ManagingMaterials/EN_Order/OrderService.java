package com.CSDLPT.ManagingMaterials.EN_Order;

import com.CSDLPT.ManagingMaterials.EN_Order.dtos.ReqDtoOrder;
import com.CSDLPT.ManagingMaterials.EN_Order.dtos.ResDtoOrderWithImportantInfo;
import com.CSDLPT.ManagingMaterials.EN_SuppliesExportation.SuppliesExportation;
import com.CSDLPT.ManagingMaterials.EN_SuppliesExportation.dtos.ReqDtoSuppliesExportation;
import com.CSDLPT.ManagingMaterials.EN_SuppliesImportation.SuppliesImportation;
import com.CSDLPT.ManagingMaterials.EN_SuppliesImportation.dtos.ReqDtoSuppliesImportation;
import com.CSDLPT.ManagingMaterials.EN_Warehouse.WarehouseRepository;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.InnerJoinObject;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.FindingActionService;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class OrderService {

    @Service
    @RequiredArgsConstructor
    public static class BranchServices {
        private final StaticUtilMethods staticUtilMethods;
        private final FindingActionService findingActionService;
        private final OrderRepository orderRepository;
        private final WarehouseRepository warehouseRepository;

        public ModelAndView getManageOrderPage(HttpServletRequest request, Model model) {
            //--Prepare a modelAndView object to symbolize the whole page.
            ModelAndView modelAndView = staticUtilMethods
                .customResponsiveModelView(request, model, "manage-order");

            //--Check if there's a response SuppliesExportation to map into adding-form when an error occurred.
            ReqDtoOrder order = (ReqDtoOrder) model.asMap().get("submittedOrder");
            if (order != null) modelAndView.addObject("order", order);

            return modelAndView;
        }

        public ResDtoRetrievingData<ResDtoOrderWithImportantInfo> findOrder(
            HttpServletRequest request,
            ReqDtoRetrievingData<ResDtoOrderWithImportantInfo> searchingObject
        ) throws SQLException, NoSuchFieldException {
            //--Preparing data to fetch.
            searchingObject.setObjectType(ResDtoOrderWithImportantInfo.class);
            searchingObject.setSearchingTable("DatHang");
            searchingObject.setSearchingTableIdName("MasoDDH");
            searchingObject.setSortingCondition("ORDER BY MasoDDH ASC");
            searchingObject.setJoiningCondition(InnerJoinObject.mergeQuery(List.of(
                InnerJoinObject.builder().left("DatHang").right("NhanVien").fields("MANV, HO, TEN").bridge("MANV").build(),
                InnerJoinObject.builder().left("DatHang").right("Kho").fields("MAKHO, TENKho").bridge("MAKHO").build()
            )));

            return findingActionService.findingDataAndServePaginationBarFormat(request, searchingObject);
        }

        public ResDtoRetrievingData<ResDtoOrderWithImportantInfo> findOrderToServeSuppliesImportation(
            HttpServletRequest request,
            ReqDtoRetrievingData<ResDtoOrderWithImportantInfo> searchingObject
        ) throws SQLException, NoSuchFieldException {
            //--Preparing data to fetch.
            searchingObject.setObjectType(ResDtoOrderWithImportantInfo.class);
            searchingObject.setSearchingTable("DatHang");
            searchingObject.setSearchingTableIdName("MasoDDH");
            searchingObject.setSortingCondition("ORDER BY NGAY ASC");
            searchingObject.setJoiningCondition(InnerJoinObject.mergeQuery(List.of(
                InnerJoinObject.builder().left("DatHang").right("NhanVien").fields("MANV, HO, TEN").bridge("MANV").build(),
                InnerJoinObject.builder().left("DatHang").right("Kho").fields("MAKHO, TENKHO").bridge("MAKHO").build()
            )));
            return findingActionService.findingDataAndServePaginationBarFormat(request, searchingObject);
        }

        public void addOrder(ReqDtoOrder order, HttpServletRequest request) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            order.trimAllFieldValues();

            if (orderRepository
                .isExistingOrderByOrderId(connectHolder, order.getOrderId()))
                throw new DuplicateKeyException("error_order_03");

            if (!warehouseRepository.isExistingWarehouseByWarehouseId(connectHolder, order.getWarehouseIdAsFk()))
                throw new NoSuchElementException("error_warehouse_02");

            ResDtoUserInfo currentUserInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");
            //--May throw SQLException if id is already existing.
            orderRepository.save(connectHolder, Order.builder()
                .orderId(order.getOrderId())
                .supplier(order.getSupplier())
                .warehouseId(order.getWarehouseIdAsFk())
                .employeeId(currentUserInfo.getEmployeeId())
                .createdDate(new Date())
                .build()
            );

            //--Close connection
            connectHolder.removeConnection();
        }

        public void updateOrder(
            ReqDtoOrder order,
            HttpServletRequest request
        ) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            order.trimAllFieldValues();

            if (orderRepository.findById(connectHolder, order.getOrderId()).isEmpty())
                throw new NoSuchElementException("error_order_01");

            if (!warehouseRepository.isExistingWarehouseByWarehouseId(connectHolder, order.getWarehouseIdAsFk()))
                throw new NoSuchElementException("error_warehouse_02");

            //--May throw SQLException if id is already existing.
            if (orderRepository.updateById(connectHolder, Order.builder()
                .orderId(order.getOrderId())
                .supplier(order.getSupplier())
                .warehouseId(order.getWarehouseIdAsFk())
                .build()) == 0)
                throw new SQLException("There's an error with SQL Server!");

            //--Close connection
            connectHolder.removeConnection();
        }

        public void deleteOrder(String orderId, HttpServletRequest request) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            if (orderRepository.findById(connectHolder, orderId).isEmpty())
                throw new NoSuchElementException("Order Id is invalid");

            if (orderRepository.deleteById(connectHolder, orderId) == 0)
                throw new SQLException("There's an error with SQL Server!");

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
