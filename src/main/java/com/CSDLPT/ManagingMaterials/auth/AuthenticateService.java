package com.CSDLPT.ManagingMaterials.auth;

import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.EN_Account.Account;
import com.CSDLPT.ManagingMaterials.EN_Account.AccountRepository;
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

    public void authenticate(Account account, HttpServletRequest request) throws SQLException, NoSuchElementException {
        ResDtoUserInfo userInfo = accountRepository.authenticate(account);

        HttpSession session = request.getSession();
        session.setAttribute("userInfo", userInfo);
    }
}
