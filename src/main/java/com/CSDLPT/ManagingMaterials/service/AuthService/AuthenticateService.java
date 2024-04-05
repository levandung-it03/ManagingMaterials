package com.CSDLPT.ManagingMaterials.service.AuthService;

import com.CSDLPT.ManagingMaterials.dto.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.model.Account;
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

    public void authenticate(Account account, HttpServletRequest request) throws SQLException, NoSuchElementException {
        ResDtoUserInfo userInfo = accountRepository.authenticate(account);

        HttpSession session = request.getSession();
        session.setAttribute("userInfo", userInfo);
    }
}
