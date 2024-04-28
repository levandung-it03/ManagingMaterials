package com.CSDLPT.ManagingMaterials.controller.BranchController;

import com.CSDLPT.ManagingMaterials.service.EmployeeService;
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

    @GetMapping("/employee/manage-employee")
    public ModelAndView getManageEmployeePage(HttpServletRequest request, Model model) throws SQLException {
        return employeeService.getManageEmployeePage(request, model);
    }

}
