package com.CSDLPT.ManagingMaterials.EN_SuppliesExportationDetail;

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
import java.util.NoSuchElementException;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class SuppliesExportationDetailController {
    private final SuppliesExportationDetailService.BranchServices branchServices;
    private final Validator hibernateValidator;
    private final Logger logger;

    /** Spring MVC: Branch-role controllers **/
    /*_____________RequestMethod.GET: Header-pages_____________*/
    @GetMapping("/branch/supplies-exportation-detail/manage-supplies-exportation-detail")
    public ModelAndView getManageSuppliesExportationDetailPage(HttpServletRequest request, Model model) {
        return branchServices.getManageSuppliesExportationDetailPage(request, model);
    }

    /*_____________RequestMethod.POST: Supplies-Exportation-Detail-entity-interaction_____________*/
    @PostMapping("${url.post.branch.prefix.v1}/find-supplies-exportation-detail-by-values")
    public ResponseEntity<ResDtoRetrievingData<SuppliesExportationDetail>> findingSuppliesExportationDetailByValues(
        @RequestBody ReqDtoRetrievingData<SuppliesExportationDetail> searchingObject,
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(branchServices.findingSuppliesExportationDetail(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }

    @PostMapping("${url.post.branch.prefix.v1}/add-supplies-exportation-detail")
    public String addSuppliesExportationDetail(
        @ModelAttribute("exportationDetail") SuppliesExportationDetail exportationDetail,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<SuppliesExportationDetail>> violations = hibernateValidator.validate(exportationDetail);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            redirectAttributes.addFlashAttribute("submittedSuppliesExportationDetail", exportationDetail);
            return "redirect:" + standingUrl;
        }

        try {
            branchServices.addSuppliesExportationDetail(exportationDetail, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_add_01");
        } catch (DuplicateKeyException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_supply_01");
            redirectAttributes.addFlashAttribute("submittedSuppliesExportationDetail", exportationDetail);
        } catch (SQLException e) {
            logger.info("Error from AddSuppliesExportationDetailController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_03");
            redirectAttributes.addFlashAttribute("submittedSuppliesExportationDetail", exportationDetail);
        } catch (Exception e) {
            logger.info("Error from AddSuppliesExportationDetailController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
            redirectAttributes.addFlashAttribute("submittedSuppliesExportationDetail", exportationDetail);
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping("${url.post.branch.prefix.v1}/update-supplies-exportation-detail")
    public String updateSuppliesExportationDetail(
        @ModelAttribute("exportationDetail") SuppliesExportationDetail exportationDetail,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<SuppliesExportationDetail>> violations = hibernateValidator.validate(exportationDetail);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            redirectAttributes.addFlashAttribute("submittedSuppliesExportationDetail", exportationDetail);
            return "redirect:" + standingUrl;
        }

        try {
            branchServices.updateSuppliesExportationDetail(exportationDetail, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_update_01");
        } catch (SQLException e) {
            logger.info("Error from UpdateSuppliesExportationDetailController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_03");
        } catch (Exception e) {
            logger.info("Error from UpdateSuppliesExportationDetailController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping("${url.post.branch.prefix.v1}/delete-supplies-exportation-detail")
    public String deleteSuppliesExportationDetail(
        @RequestParam("deleteBtn") String exportationDetailId,
        @RequestParam("supplyId") String supplyId,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        try {
            branchServices.deleteSuppliesExportationDetail(exportationDetailId, supplyId, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_delete_01");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_01");
        } catch (SQLException e) {
            logger.info("Error from deleteSuppliesExportationDetail: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_03");
        } catch (Exception e) {
            logger.info("Error from deleteSuppliesExportationDetail: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + request.getHeader("Referer");
    }
}
