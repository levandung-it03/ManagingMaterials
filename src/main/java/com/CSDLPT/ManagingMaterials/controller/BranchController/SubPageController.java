package com.CSDLPT.ManagingMaterials.controller.BranchController;

import com.CSDLPT.ManagingMaterials.service.BranchService.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("${url.get.branch.prefix}/sub-page")
public class SubPageController {
    private final SubPageService subPageService;

    @GetMapping("/manage-order-detail")
    public ModelAndView getManageOrderDetailPage(HttpServletRequest request, Model model) {
        return subPageService.getManageOrderDetailPage(request, model);
    }
}
