package com.CSDLPT.ManagingMaterials.EN_Account;

import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ReqDtoAddingAccount;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class AccountService {

    @Service
    @RequiredArgsConstructor
    public static class PublicServices {
        private final StaticUtilMethods staticUtilMethods;

        public ModelAndView getLoginPage(HttpServletRequest request, Model model) {
            if (request.getSession().getAttribute("userInfo") == null) {
                return staticUtilMethods.customResponsiveModelView(request, model, "login");
            } else {
                return new ModelAndView("redirect:/home");
            }
        }

        public ModelAndView getHomePage(HttpServletRequest request, Model model) {
            ModelAndView modelAndView = staticUtilMethods.customResponsiveModelView(request, model, "home");
            ResDtoUserInfo userInfo = (ResDtoUserInfo) request
                .getSession()
                .getAttribute("userInfo");

            modelAndView.addObject("userInfo", userInfo);

            return modelAndView;
        }
    }

    @Service
    @RequiredArgsConstructor
    public static class AuthenticatedServices {
        private final AccountRepository accountRepository;

        public String checkIfEmployeeAccountIsExisting(HttpServletRequest request, String employeeId) throws SQLException {
            //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
            DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            boolean result = accountRepository.checkIfEmployeeAccountIsExisting(connectionHolder, employeeId);

            //--Close Connection.
            connectionHolder.removeConnection();

            return "{\"isExistingEmployeeAccount\": " + result + "}";
        }

        public void addAccount(HttpServletRequest request, ReqDtoAddingAccount account) throws SQLException {
            //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
            DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            ResDtoUserInfo userInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");
            if (userInfo.getRole().equals(RoleEnum.CONGTY) && !account.getRole().equals("CONGTY"))
                throw new SQLIntegrityConstraintViolationException();

            if (account.getRole().equals("CONGTY") && !userInfo.getRole().equals(RoleEnum.CONGTY))
                throw new SQLIntegrityConstraintViolationException();

            accountRepository.save(connectionHolder, account);

            //--Close Connection.
            connectionHolder.removeConnection();
        }
    }



    @Service
    public static class BranchServices {

    }

    @Service
    public static class CompanyServices {

    }

    @Service
    public static class UserServices {

    }
}
