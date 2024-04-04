package com.CSDLPT.ManagingMaterials.service;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.model.Employee;
import com.CSDLPT.ManagingMaterials.repository.EmployeeRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final StaticUtilMethods staticUtilMethods;
    private final EmployeeRepository employeeRepository;

    public ModelAndView getManageEmployee(HttpServletRequest request, Model model) {
        ModelAndView modelAndView = staticUtilMethods.customResponseModelView(request, model.asMap(), "manage-employee");

        Employee employee = (Employee) model.asMap().get("employee");

        if (employee != null)
            modelAndView.addObject(employee);
        return modelAndView;
    }

    public void addEmployee(HttpServletRequest request, Employee employee) throws DuplicateKeyException, SQLException {
        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        //--Get Current_Login_User from Session.
        Employee currentAccountInfo = (Employee) request.getSession().getAttribute("employeeInfo");

        //--Check If 'MANV' is already existing or not.
        Optional<Employee> existedResult = employeeRepository.findById(connectHolder, employee.getEmployeeId());
        if (existedResult.isPresent())  throw new DuplicateKeyException("Can't add an existing employee!");

        //--Employee must have the same 'branch' with Current_Login_User.
        employee.setBranch(currentAccountInfo.getBranch());

        //--May throw DuplicateKeyException with ('CNMD', 'TrangThaiXoa') pair of fields.
        if (employeeRepository.save(connectHolder, employee) == 0) {
            throw new SQLException("There's an error with SQL Server!");
        }
    }
}
