package com.CSDLPT.ManagingMaterials.controller.AuthController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.ui.Model;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.model.Account;
import com.CSDLPT.ManagingMaterials.service.AuthService.AuthenticateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Controller
@RequestMapping("${url.post.auth.prefix.v1}")
public class AuthenticateController {
    private final AuthenticateService authenticateService;
    private final Logger logger;

    @PostMapping("/authenticate")
    public String authenticate(
            @Valid @ModelAttribute("account") Account account,
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
        } catch (NoSuchElementException | SQLException ignored) {
            redirectAttributes.addFlashAttribute("errorCode", "error_account_01");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
            logger.info(String.valueOf(e));
        }
        return "redirect:/login";
    }
}
