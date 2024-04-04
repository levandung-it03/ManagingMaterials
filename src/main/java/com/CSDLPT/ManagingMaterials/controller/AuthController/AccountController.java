package com.CSDLPT.ManagingMaterials.controller.AuthController;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class AccountController {
    private final StaticUtilMethods staticUtilMethods;

    @GetMapping("/login")
    public ModelAndView getLoginPage(
        HttpServletRequest request,
        HttpServletResponse response,
        Model model
    ) throws IOException {
        if (request.getSession().getAttribute("employeeInfo") == null) {
            return staticUtilMethods.customResponseModelView(request, model.asMap(), "login");
        } else {
            response.sendRedirect("/home");
            return null;
        }
    }
}
