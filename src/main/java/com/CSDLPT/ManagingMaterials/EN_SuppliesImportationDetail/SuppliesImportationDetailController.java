package com.CSDLPT.ManagingMaterials.EN_SuppliesImportationDetail;

import com.CSDLPT.ManagingMaterials.EN_SuppliesImportation.SuppliesImportation;
import com.CSDLPT.ManagingMaterials.EN_SuppliesImportation.dtos.ReqDtoSuppliesImportation;
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
public class SuppliesImportationDetailController {
    private final SuppliesImportationDetailService.BranchServices branchServices;
    private final Validator hibernateValidator;
    private final Logger logger;

    /** Spring MVC: Branch-role controllers **/
    /*_____________RequestMethod.GET: Header-pages_____________*/
    @GetMapping("/branch/supplies-importation-detail/manage-supplies-importation-detail")
    public ModelAndView getManageSuppliesImportationDetailPage(HttpServletRequest request, Model model) throws SQLException {
        return branchServices.getManageSuppliesImportationDetailPage(request, model);
    }

    /*_____________RequestMethod.POST: Supplies-Importation-Detail-entity-interaction_____________*/
    @PostMapping("${url.post.branch.prefix.v1}/find-supplies-importation-detail-by-values")
    public ResponseEntity<ResDtoRetrievingData<SuppliesImportationDetail>> findingSuppliesImportationsDetailByValues(
        @RequestBody ReqDtoRetrievingData<SuppliesImportationDetail> searchingObject,
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(branchServices.findingSuppliesImportationsDetail(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }

    @PostMapping("${url.post.branch.prefix.v1}/add-supplies-importation-detail")
    public String addSuppliesImportationsDetail(
        @ModelAttribute("importationDetail") SuppliesImportationDetail importationDetail,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<SuppliesImportationDetail>> violations = hibernateValidator.validate(importationDetail);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            redirectAttributes.addFlashAttribute("submittedSuppliesImportationDetail", importationDetail);
            return "redirect:" + standingUrl;
        }

        try {
            branchServices.addSuppliesImportationDetail(importationDetail, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_add_01");
        } catch (DuplicateKeyException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_supply_01");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_03");
        } catch (Exception e) {
            logger.info("Error from AddSuppliesImportationDetailController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping("${url.post.branch.prefix.v1}/update-supplies-importation-detail")
    public String updateSuppliesImportationsDetail(
        @ModelAttribute("importationDetail") SuppliesImportationDetail importationDetail,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<SuppliesImportationDetail>> violations = hibernateValidator.validate(importationDetail);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            redirectAttributes.addFlashAttribute("submittedSuppliesImportationDetail", importationDetail);
            return "redirect:" + standingUrl;
        }

        try {
            branchServices.updateSuppliesImportationDetail(importationDetail, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_update_01");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_03");
        } catch (SQLIntegrityConstraintViolationException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_supply_03");
        } catch (Exception e) {
            logger.info("Error from UpdateSuppliesImportationsDetailController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + standingUrl;
    }

}
