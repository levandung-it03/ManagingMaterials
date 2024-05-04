package com.CSDLPT.ManagingMaterials.controller.BranchController;

import com.CSDLPT.ManagingMaterials.service.BranchService.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("${url.post.branch.prefix.v1}")
public class AccountController {
    private final AccountService accountService;
    private final Logger logger;

    @PostMapping("/check-if-employee-account-is-existing")
    public ResponseEntity<HashMap<String, Boolean>> checkIfEmployeeAccountIsExisting(
        @RequestParam("employeeId") String employeeId,
        HttpServletRequest request
    ) {
        try {
                return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.checkIfEmployeeAccountIsExisting(request, employeeId));
        } catch (Exception e) {
            logger.info(e.toString());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(null);
        }
    }
}
