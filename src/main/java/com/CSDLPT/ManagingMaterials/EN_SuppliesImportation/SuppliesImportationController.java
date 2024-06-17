package com.CSDLPT.ManagingMaterials.EN_SuppliesImportation;

import com.CSDLPT.ManagingMaterials.EN_SuppliesImportation.dtos.ReqDtoSuppliesImportation;
import com.CSDLPT.ManagingMaterials.EN_SuppliesImportation.dtos.ResDtoImportationWithImportationInfo;
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

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class SuppliesImportationController {
    private final SuppliesImportationService.BranchServices branchServices;
    private final Validator hibernateValidator;
    private final Logger logger;

    /** Spring MVC: Branch-role controllers **/
    /*_____________RequestMethod.GET: Header-pages_____________*/
    @GetMapping("/branch/supplies-importation/manage-supplies-importation")
    public ModelAndView getManageSuppliesImportationPage(HttpServletRequest request, Model model) throws SQLException {
        return branchServices.getManageSuppliesImportationPage(request, model);
    }

    /*_____________RequestMethod.POST: Supplies-Importation-entity-interaction_____________*/
    @PostMapping("${url.post.branch.prefix.v1}/find-supplies-importation-by-values")
    public ResponseEntity<ResDtoRetrievingData<ResDtoImportationWithImportationInfo>> findingSuppliesImportationsByValues(
        @RequestBody ReqDtoRetrievingData<ResDtoImportationWithImportationInfo> searchingObject,
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

    @PostMapping("${url.post.branch.prefix.v1}/add-supplies-importation")
    public String addSuppliesImportation(
        @ModelAttribute("suppliesImportation") ReqDtoSuppliesImportation importation,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<ReqDtoSuppliesImportation>> violations = hibernateValidator.validate(importation);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            redirectAttributes.addFlashAttribute("submittedSuppliesImportation", importation);
            return "redirect:" + standingUrl;
        }

        try {
            branchServices.addSuppliesImportation(importation, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_add_01");
        } catch (NoSuchElementException | DuplicateKeyException e) {
            redirectAttributes.addFlashAttribute("errorCode", e.getMessage());
            redirectAttributes.addFlashAttribute("submittedSuppliesImportation", importation);
        } catch (Exception e) {
            logger.info("Error from AddSuppliesImportationController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
            redirectAttributes.addFlashAttribute("submittedSuppliesImportation", importation);
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping("${url.post.branch.prefix.v1}/update-supplies-importation")
    public String updateSuppliesImportation(
        @ModelAttribute("suppliesImportation") ReqDtoSuppliesImportation importation,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<ReqDtoSuppliesImportation>> violations = hibernateValidator.validate(importation);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            return "redirect:" + standingUrl;
        }

        try {
            branchServices.updateSuppliesImportation(importation, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_update_01");
        } catch (NoSuchElementException | DuplicateKeyException e) {
            redirectAttributes.addFlashAttribute("errorCode", e.getMessage());
        } catch (Exception e) {
            logger.info("Error from UpdateSuppliesImportationController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping("${url.post.branch.prefix.v1}/delete-supplies-importation")
    public String deleteSuppliesImportation(
        @RequestParam("deleteBtn") String importationId,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        try {
            branchServices.deleteSuppliesImportation(importationId, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_delete_01");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorCode", e.getMessage());
        } catch (SQLIntegrityConstraintViolationException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_suppliesImportation_03");
        } catch (Exception e) {
            logger.info("Error from UpdateSuppliesImportationController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + request.getHeader("Referer");
    }
}
