package com.CSDLPT.ManagingMaterials.service.AuthService;

import com.CSDLPT.ManagingMaterials.dto.ResDtoEmployeeInfo;
import com.CSDLPT.ManagingMaterials.dto.ReqDtoAccount;
import com.CSDLPT.ManagingMaterials.repository.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthenticateService {
    private final AccountRepository accountRepository;

    public void authenticate(ReqDtoAccount account, HttpServletRequest request) throws SQLException, NoSuchElementException {
        ResDtoEmployeeInfo employeeInfo = accountRepository.authenticate(account);

        HttpSession session = request.getSession();
        session.setAttribute("id", employeeInfo.getEmployeeId());
        session.setAttribute("fullName", employeeInfo.getFullName());
        session.setAttribute("role", employeeInfo.getRole());
        session.setAttribute("branch", account.getBranch());
        session.setAttribute("key", account.getPassword());
    }
}
