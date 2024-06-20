package com.CSDLPT.ManagingMaterials.EN_Account;

import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ReqDtoAddingAccount;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService.PublicServices publicServices;
    private final AccountService.AuthenticatedServices authenticatedServices;
    private final Validator hibernateValidator;
    private final Logger logger;

    /** Spring MVC: Public controllers **/
    /*_____________RequestMethod.GET: Public-components_____________*/
    @GetMapping("/login")
    public ModelAndView getLoginPage(HttpServletRequest request, Model model) {
        return publicServices.getLoginPage(request, model);
    }

    /*_________________________________________Role_Components_Separator____________________________________________
     *____________________________________________________________________________________________________________*/


    /** Spring MVC: Auth-role controllers **/
    /*_____________RequestMethod.POST: Account-entity-interaction_____________*/
    @PostMapping({"${url.post.branch.prefix.v1}/check-if-employee-account-is-existing",
        "${url.post.company.prefix.v1}/check-if-employee-account-is-existing"})
    public ResponseEntity<String> checkIfEmployeeAccountIsExisting(
        @RequestParam("employeeId") String employeeId,
        HttpServletRequest request
    ) {
        try {
                return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticatedServices.checkIfEmployeeAccountIsExisting(request, employeeId));
        } catch (Exception e) {
            logger.info(e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @PostMapping({"${url.post.branch.prefix.v1}/add-account",
        "${url.post.company.prefix.v1}/add-account"})
    public String addAccount(
        @ModelAttribute ReqDtoAddingAccount account,
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
            authenticatedServices.addAccount(request, account);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_add_01");
        } catch (DuplicateKeyException ignored) {
            redirectAttributes.addFlashAttribute("errorCode", "error_account_02");
        } catch (Exception ignored) {
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + standingUrl;
    }
}
