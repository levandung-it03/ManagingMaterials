package com.CSDLPT.ManagingMaterials.service.BranchService;

import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.repository.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public HashMap<String, Boolean> checkIfEmployeeAccountIsExisting(HttpServletRequest request, String employeeId) throws SQLException {
        HashMap<String, Boolean> result = new HashMap<>();

        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        if (accountRepository.checkIfEmployeeAccountIsExisting(connectionHolder, employeeId))
            result.put("isExistingEmployeeAccount", true);
        else
            result.put("isExistingEmployeeAccount", false);

        //--Close Connection.
        connectionHolder.removeConnection();

        return result;
    }
}
