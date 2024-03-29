package com.CSDLPT.ManagingMaterials.controller.AuthController;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.ui.Model;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.dto.ReqDtoAccount;
import com.CSDLPT.ManagingMaterials.service.AuthService.AuthenticateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Controller
public class AuthenticateController {
    private final AuthenticateService authenticateService;
    private final StaticUtilMethods staticUtilMethods;
    private final Logger logger;

    @GetMapping("/login")
    public ModelAndView getLoginPage(Model model) {
        return staticUtilMethods.customResponseModelView(model.asMap(), "login");
    }

    @PostMapping("/service/v1/auth/authenticate")
    public String authenticate(
            @Valid @ModelAttribute("account") ReqDtoAccount account,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorCode", bindingResult.getFieldErrors().getFirst());
            return "redirect:/login";
        }

        try {
            authenticateService.authenticate(account, request);
            return "redirect:/home";
        } catch (NoSuchElementException ignored) {
            redirectAttributes.addFlashAttribute("errorCode", "error_account_03");
        } catch (SQLException e) {
//            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
            logger.info(e.getMessage());
        }
        return "redirect:/login";
    }
}
