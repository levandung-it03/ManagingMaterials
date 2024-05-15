package com.CSDLPT.ManagingMaterials.EN_Employee;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.EN_Branch.BranchRepository;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.FindingActionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

public class EmployeeService {

    @Service
    @RequiredArgsConstructor
    public static class BranchServices {
        private final StaticUtilMethods staticUtilMethods;
        private final EmployeeRepository employeeRepository;
        private final BranchRepository branchRepository;
        private final FindingActionService findingActionService;

        public ModelAndView getManageEmployeePage(HttpServletRequest request, Model model) throws SQLException {
            //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
            DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            //--Prepare common-components of ModelAndView if we need.
            ModelAndView modelAndView = staticUtilMethods
                .customResponsiveModelView(request, model, "manage-employee");
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

            //--Data for AddingEmployeeForm component.
            modelAndView.addObject("branchesList", branchesList);

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

        public ResDtoRetrievingData<Employee> findEmployee(
            HttpServletRequest request,
            ReqDtoRetrievingData<Employee> searchingObject
        ) throws SQLException, NoSuchFieldException {
            //--Preparing data to fetch.
            searchingObject.setObjectType(Employee.class);
            searchingObject.setSearchingTable("NhanVien");
            searchingObject.setSearchingTableIdName("MANV");
            searchingObject.setMoreCondition("TrangThaiXoa = 0");
            searchingObject.setSortingCondition("ORDER BY MANV DESC, TEN ASC, HO DESC");

            return findingActionService.findingDataAndServePaginationBarFormat(request, searchingObject);
        }

        public void updateEmployee(Employee employee, HttpServletRequest request) throws SQLException {
            //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
            DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

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

            //--Close Connection.
            connectionHolder.removeConnection();
        }
    }

    @Service
    public static class CompanyServices {

    }

    @Service
    public static class UserServices {

    }
}