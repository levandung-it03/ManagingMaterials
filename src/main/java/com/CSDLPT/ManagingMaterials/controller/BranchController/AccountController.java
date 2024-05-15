package com.CSDLPT.ManagingMaterials.controller.BranchController;

import com.CSDLPT.ManagingMaterials.dto.ReqDtoAddingAccount;
import com.CSDLPT.ManagingMaterials.service.BranchService.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("${url.post.branch.prefix.v1}")
public class AccountController {
    private final AccountService accountService;
    private final Validator hibernateValidator;
    private final Logger logger;

    @PostMapping("/check-if-employee-account-is-existing")
    public ResponseEntity<String> checkIfEmployeeAccountIsExisting(
        @RequestParam("employeeId") String employeeId,
        HttpServletRequest request
    ) {
        try {
                return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.checkIfEmployeeAccountIsExisting(request, employeeId));
        } catch (Exception e) {
            logger.info(e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/add-account")
    public String addAccount(
        @RequestBody ReqDtoAddingAccount account,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<ReqDtoAddingAccount>> violations = hibernateValidator.validate(account);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            return "redirect:" + standingUrl;
        }

        try {
            accountService.addAccount(request, account);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_add_01");
        } catch (DuplicateKeyException ignored) {
            redirectAttributes.addFlashAttribute("errorCode", "error_account_02");
        } catch (Exception ignored) {
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + standingUrl;
    }
}
