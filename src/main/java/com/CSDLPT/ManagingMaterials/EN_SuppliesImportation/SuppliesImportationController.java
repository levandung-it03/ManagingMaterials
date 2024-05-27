package com.CSDLPT.ManagingMaterials.EN_SuppliesImportation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class SuppliesImportationController {
    private final SuppliesImportationService.BranchServices branchServices;
    private final Validator hibernateValidator;
    private final Logger logger;

    /** Spring MVC: Branch-role controllers **/
    /*_____________RequestMethod.GET: Header-pages_____________*/
    public ModelAndView getManageWarehousePage(HttpServletRequest request, Model model) {
        return branchServices.getManageSuppliesImportationPage(request, model);
    }

    /*_____________RequestMethod.POST: Supplies-Importation-entity-interaction_____________*/
}
