package com.CSDLPT.ManagingMaterials.EN_SuppliesExportation;

import com.CSDLPT.ManagingMaterials.EN_SuppliesExportation.dtos.ReqDtoSuppliesExportation;
import com.CSDLPT.ManagingMaterials.EN_SuppliesExportation.dtos.ResDtoExportationWithImportationInfo;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
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

import java.util.NoSuchElementException;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class SuppliesExportationController {
    private final SuppliesExportationService.BranchServices branchServices;
    private final Validator hibernateValidator;
    private final Logger logger;

    /** Spring MVC: Branch-role controllers **/
    /*_____________RequestMethod.GET: Header-pages_____________*/
    @GetMapping("/branch/supplies-exportation/manage-supplies-exportation")
    public ModelAndView getManageSuppliesExportationPage(HttpServletRequest request, Model model) {
        try {
            return branchServices.getManageSuppliesExportationPage(request, model);
        } catch (Exception e) {
            logger.info("Error from 'getManageSuppliesExportationPage' in 'SuppliesExportationController': " + e);
            return null;
        }
    }
    
    /*_____________RequestMethod.POST: Supplies-Exportation-entity-interaction_____________*/
    @PostMapping("${url.post.branch.prefix.v1}/find-supplies-exportation-by-values")
    public ResponseEntity<ResDtoRetrievingData<ResDtoExportationWithImportationInfo>> findingSuppliesExportationByValues(
        @RequestBody ReqDtoRetrievingData<ResDtoExportationWithImportationInfo> searchingObject,
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(branchServices.findingSuppliesExportation(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }

    @PostMapping("${url.post.branch.prefix.v1}/add-supplies-exportation")
    public String addSuppliesImportation(
        @ModelAttribute("suppliesExportation") ReqDtoSuppliesExportation exportation,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<ReqDtoSuppliesExportation>> violations = hibernateValidator.validate(exportation);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            redirectAttributes.addFlashAttribute("submittedSuppliesExportation", exportation);
            return "redirect:" + standingUrl;
        }

        try {
            branchServices.addSuppliesExportation(exportation, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_add_01");
        } catch (NoSuchElementException | DuplicateKeyException e) {
            redirectAttributes.addFlashAttribute("errorCode", e.getMessage());
            redirectAttributes.addFlashAttribute("submittedSuppliesExportation", exportation);
        } catch (Exception e) {
            logger.info("Error from AddSuppliesExportationController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
            redirectAttributes.addFlashAttribute("submittedSuppliesExportation", exportation);
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping("${url.post.branch.prefix.v1}/update-supplies-exportation")
    public String updateSuppliesImportation(
        @ModelAttribute("suppliesExportation") ReqDtoSuppliesExportation exportation,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<ReqDtoSuppliesExportation>> violations = hibernateValidator.validate(exportation);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            return "redirect:" + standingUrl;
        }

        try {
            branchServices.updateSuppliesExportation(exportation, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_update_01");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorCode", e.getMessage());
        } catch (Exception e) {
            logger.info("Error from UpdateSuppliesExportationController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping("${url.post.branch.prefix.v1}/delete-supplies-exportation")
    public String deleteSuppliesExportation(
        @RequestParam("deleteBtn") String exportationId,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        try {
            branchServices.deleteSuppliesExportation(exportationId, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_delete_01");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_suppliesExportation_02");
        } catch (Exception e) {
            logger.info("Error from DeleteSuppliesExportationController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + request.getHeader("Referer");
    }


}
