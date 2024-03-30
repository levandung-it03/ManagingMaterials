package com.CSDLPT.ManagingMaterials.controller;

import com.CSDLPT.ManagingMaterials.dto.ResDtoEmployeeInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/management")
public class HomeController {

    @GetMapping(path = "/home")
    public ModelAndView getHomePage(HttpServletRequest request, Model model) {
        ModelAndView modelAndView = new ModelAndView("home");
        ResDtoEmployeeInfo employeeInfo = (ResDtoEmployeeInfo) request
            .getSession()
            .getAttribute("employeeInfo");

        employeeInfo.setPassword(null);
        modelAndView.addObject("employeeInfo", employeeInfo);

        return modelAndView;
    }
}
