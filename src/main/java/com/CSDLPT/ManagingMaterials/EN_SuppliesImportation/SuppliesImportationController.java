package com.CSDLPT.ManagingMaterials.EN_SuppliesImportation;

import com.CSDLPT.ManagingMaterials.EN_Warehouse.Warehouse;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class SuppliesImportationController {
    private final SuppliesImportationService.BranchServices branchServices;
    private final Validator hibernateValidator;
    private final Logger logger;

    /** Spring MVC: Branch-role controllers **/
    /*_____________RequestMethod.GET: Header-pages_____________*/
    @GetMapping("/branch/supplies-importation/manage-supplies-importation")
    public ModelAndView getManageWarehousePage(HttpServletRequest request, Model model) {
        return branchServices.getManageSuppliesImportationPage(request, model);
    }

    /*_____________RequestMethod.POST: Supplies-Importation-entity-interaction_____________*/
    @PostMapping("${url.post.branch.prefix.v1}/find-supplies-importation-by-values")
    public ResponseEntity<ResDtoRetrievingData<SuppliesImportation>> findingSuppliesImportationsByValues(
        @RequestBody ReqDtoRetrievingData<SuppliesImportation> searchingObject,
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(branchServices.findSuppliesImportation(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }
}
