package com.CSDLPT.ManagingMaterials.service;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.dto.ReqDtoFindingAction;
import com.CSDLPT.ManagingMaterials.dto.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.model.Employee;
import com.CSDLPT.ManagingMaterials.model.PageObject;
import com.CSDLPT.ManagingMaterials.repository.BranchRepository;
import com.CSDLPT.ManagingMaterials.repository.EmployeeRepository;
import com.CSDLPT.ManagingMaterials.service.GeneralService.FindingActionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final StaticUtilMethods staticUtilMethods;
    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final FindingActionService findingActionService;

    public ModelAndView getManageEmployeePage(HttpServletRequest request, Model model) throws SQLException {
        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        //--Prepare common-components of ModelAndView if we need.
        ModelAndView modelAndView = staticUtilMethods.customResponseModelView(request, model.asMap(), "manage-employee");
        List<String> branchesList = branchRepository.findAllBranchIds(connectionHolder);

        //--If there's an error when handle data with DB, take the submitted-employee-info and give it back to this page
        Employee employee = (Employee) model.asMap().get("submittedEmployee");
        if (employee != null)   modelAndView.addObject("employee", employee);
        //--If the redirected employee-info doesn't exist, so this is the plain (empty) adding-form.
        else {
            //--Get Current_Login_User from Session.
            ResDtoUserInfo userInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");
            Integer nextEmployeeId = employeeRepository.getNextEmployeeId(connectionHolder, userInfo.getBranch());

            //--Save auto-generated into session for AddEmployeeAction.
            request.getSession().setAttribute("addingEmployeeId", nextEmployeeId);

            //--Give the auto-generated employeeId to user.
            modelAndView.addObject("employee", Employee.builder().employeeId(nextEmployeeId).build());
        }

        //--Prepare data of employee-list.
        PageObject pageObj = new PageObject(request);
        List<Employee> employeeList = employeeRepository.findAll(connectionHolder, pageObj);

        //--Data for EmployeeList component.
        modelAndView.addObject("employeeList", employeeList);
        modelAndView.addObject("branchesList", branchesList);
        modelAndView.addObject("currentPage", pageObj.getPage());

        //--Close Connection.
        connectionHolder.removeConnection();

        return modelAndView;
    }

    public void addEmployee(HttpServletRequest request, Employee employee) throws DuplicateKeyException, SQLException {
        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        //--Get the auto-generated employeeId from Session (from getManageEmployeePage()).
        String autoEmployeeIdFromSession = request.getSession().getAttribute("addingEmployeeId").toString();
        employee.setEmployeeId(Integer.parseInt(autoEmployeeIdFromSession));

        //--Get Current_Login_User from Session.
        ResDtoUserInfo userInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");

        //--Check If 'MANV' is already existing or not.
        if (employeeRepository.isExistingEmployeeByIdentifier(connectionHolder, employee.getIdentifier()))
            throw new DuplicateKeyException("Can't add an existing employee!");

        //--Employee must have the same 'branch' with Current_Login_User.
        employee.setBranch(userInfo.getBranch());

        //--May throw DuplicateKeyException with ('CNMD', 'TrangThaiXoa') pair of fields.
        if (employeeRepository.save(connectionHolder, employee) == 0)
            throw new DuplicateKeyException("There's an error with SQL Server!");

        //--Close Connection.
        connectionHolder.removeConnection();
    }

    public List<Employee> findEmployee(HttpServletRequest request, ReqDtoFindingAction<Employee> searchingObject) {
        searchingObject.setObjectType(Employee.class);
        searchingObject.setSearchingTable("NHANVIEN");
        searchingObject.setMoreCondition("TrangThaiXoa = 0");
        searchingObject.setSortingCondition("ORDER BY MANV DESC, TEN ASC, HO DESC");
        return findingActionService.findingDataWithPaging(request, searchingObject);
    }

    public void updateEmployee(Employee employee, HttpServletRequest request) throws SQLException {
        final int updatedId = Integer.parseInt(request.getParameter("employeeId"));

        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        if (updatedId != employee.getEmployeeId())
            throw new NoSuchElementException("Employee Id is invalid");

        //--Get the old-branch from updating-action-owner (the logging-in user has the same branch with this employee)
        ResDtoUserInfo userInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");

        if (employeeRepository.update(connectionHolder, employee, userInfo.getBranch()) == 0)
            throw new SQLException("There's an error with SQL Server!");

        //--Close Connection.
        connectionHolder.removeConnection();
    }

    public void deleteEmployee(String employeeId, HttpServletRequest request) throws SQLException {
        final int deletedId = Integer.parseInt(employeeId);

        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        if (employeeRepository.delete(connectionHolder, deletedId) == 0)
            throw new SQLException("Something wrong happened in your DBMS");
    }
}