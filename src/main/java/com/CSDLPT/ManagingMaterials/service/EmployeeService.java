package com.CSDLPT.ManagingMaterials.service;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.dto.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.model.Employee;
import com.CSDLPT.ManagingMaterials.model.PageObject;
import com.CSDLPT.ManagingMaterials.repository.BranchRepository;
import com.CSDLPT.ManagingMaterials.repository.EmployeeRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final StaticUtilMethods staticUtilMethods;
    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;

    public ModelAndView getManageEmployeePage(HttpServletRequest request, Model model) {
        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        //--Prepare common-components of ModelAndView if we need.
        ModelAndView modelAndView = staticUtilMethods.customResponseModelView(request, model.asMap(), "manage-employee");

        //--If there's an error when handle data with DB, take the submitted-employee-info and give it back to this page
        Employee employee = (Employee) model.asMap().get("submittedEmployee");
        if (employee != null) {
            modelAndView.addObject("employee", employee);
        }
        //--If the redirected employee-info doesn't exist, so this is the plain (empty) adding-form.
        else {
            Integer lastEmployeeId = employeeRepository.findTheLastEmployeeId(connectionHolder);
            Integer branchesQuantity = branchRepository.countAll(connectionHolder);

            //--Save auto-generated into session for AddEmployeeAction.
            request.getSession().setAttribute("addingEmployeeId", lastEmployeeId + branchesQuantity);

            //--Give the auto-generated employee-id to user.
            modelAndView.addObject("employee", Employee.builder()
                    .employeeId(lastEmployeeId + branchesQuantity)
                    .build()
            );
        }

        //--Prepare data of employee-list.
        PageObject pageObj = new PageObject(request);
        List<Employee> employeeList = employeeRepository.findAll(connectionHolder, pageObj);

        //--Data for EmployeeList component.
        modelAndView.addObject("employeeList", employeeList);
        modelAndView.addObject("currentPage", pageObj.getPage());

        return modelAndView;
    }

    public void addEmployee(HttpServletRequest request, Employee employee) throws DuplicateKeyException, SQLException {
        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        //--Get the auto-generated employee-id from Session (from getManageEmployeePage()).
        String autoEmployeeIdFromSession = request.getSession().getAttribute("addingEmployeeId").toString();
        employee.setEmployeeId(Integer.parseInt(autoEmployeeIdFromSession));

        //--Get Current_Login_User from Session.
        ResDtoUserInfo userInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");

        //--Check If 'MANV' is already existing or not.
        if (employeeRepository.isExistingEmployee(connectionHolder, employee.getIdentifier())) {
            throw new DuplicateKeyException("Can't add an existing employee!");
        }

        //--Employee must have the same 'branch' with Current_Login_User.
        employee.setBranch(userInfo.getBranch());

        //--May throw DuplicateKeyException with ('CNMD', 'TrangThaiXoa') pair of fields.
        if (employeeRepository.save(connectionHolder, employee) == 0) {
            throw new DuplicateKeyException("There's an error with SQL Server!");
        }

        //--Close Connection.
        connectionHolder.removeConnection();
    }

    public List<Employee> findInformationEmployee(HttpServletRequest request,String columnName, String searchValue){
        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
        //--Prepare data of employee-list.
        PageObject pageObj = new PageObject(request);
        return employeeRepository.findByField(connectionHolder, pageObj,columnName,searchValue);
    }
}