package com.CSDLPT.ManagingMaterials.controller.BranchController;

import com.CSDLPT.ManagingMaterials.service.BranchService.EmployeeService;
import com.CSDLPT.ManagingMaterials.service.BranchService.OrderService;
import com.CSDLPT.ManagingMaterials.service.BranchService.SupplyService;
import com.CSDLPT.ManagingMaterials.service.BranchService.WarehouseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@Controller
@RequiredArgsConstructor
@RequestMapping("${url.get.branch.prefix}")
public class HeaderPagesController {
    private final EmployeeService employeeService;
    private final SupplyService supplyService;
    private final WarehouseService warehouseService;
    private final OrderService orderService;

    @GetMapping("/employee/manage-employee")
    public ModelAndView getManageEmployeePage(HttpServletRequest request, Model model) throws SQLException {
        return employeeService.getManageEmployeePage(request, model);
    }

    @GetMapping("/supply/manage-supply")
    public ModelAndView getManageSupplyPage(HttpServletRequest request, Model model) {
        return supplyService.getManageSupplyPage(request, model);
    }

    @GetMapping("/warehouse/manage-warehouse")
    public ModelAndView getManageWarehousePage(HttpServletRequest request, Model model) {
        return warehouseService.getManageWarehousePage(request, model);
    }

    @GetMapping("/order/manage-order")
    public ModelAndView getManageOrderPage(HttpServletRequest request, Model model) {
        return orderService.getManageOrderPage(request, model);
    }
}