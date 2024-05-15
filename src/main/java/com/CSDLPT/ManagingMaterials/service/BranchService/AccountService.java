package com.CSDLPT.ManagingMaterials.service.BranchService;

import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.dto.ReqDtoAddingAccount;
import com.CSDLPT.ManagingMaterials.repository.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AccountService {
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

        if (accountRepository.save(connectionHolder, account) == 0)
            throw new DuplicateKeyException("Something wrong when adding new Account");

        //--Close Connection.
        connectionHolder.removeConnection();
    }
}
