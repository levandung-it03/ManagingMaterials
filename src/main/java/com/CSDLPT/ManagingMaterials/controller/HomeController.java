package com.CSDLPT.ManagingMaterials.controller;

import com.CSDLPT.ManagingMaterials.dto.ResDtoUserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/home")
    public ModelAndView getHomePage(HttpServletRequest request, Model model) {
        ModelAndView modelAndView = new ModelAndView("home");
        ResDtoUserInfo userInfo = (ResDtoUserInfo) request
            .getSession()
            .getAttribute("userInfo");

        modelAndView.addObject("userInfo", userInfo);

        return modelAndView;
    }
}
