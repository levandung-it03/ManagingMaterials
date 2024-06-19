package com.CSDLPT.ManagingMaterials.EN_Supply;

import com.CSDLPT.ManagingMaterials.EN_Supply.dtos.ReqDtoTicketsForDetailSuppliesReport;
import com.CSDLPT.ManagingMaterials.EN_Supply.dtos.ResDtoSupplyForImportToBuildDialog;
import com.CSDLPT.ManagingMaterials.EN_Supply.dtos.ResDtoTicketsForDetailSuppliesReport;
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
public class SupplyController {
    private final SupplyService.AuthenticatedServices authenticatedServices;
    private final Validator hibernateValidator;
    private final Logger logger;

    /** Spring MVC: Auth-role controllers **/
    /*_____________RequestMethod.GET: Header-pages_____________*/
    @GetMapping({"/branch/supply/manage-supply", "/company/supply/manage-supply","/user/supply/manage-supply"})
    public ModelAndView getManageSupplyPage(HttpServletRequest request, Model model) throws SQLException {
        return authenticatedServices.getManageSupplyPage(request, model);
    }
    @GetMapping({"/branch/supply/report-for-supply",
        "/company/supply/report-for-supply",
        "/user/supply/report-for-supply"})
    public ModelAndView getReportForSupplyPage(HttpServletRequest request, Model model) throws SQLException {
        return authenticatedServices.getReportForSupplyPage(request, model);
    }

    @GetMapping({"/branch/supply/report-for-detail-supplies-interact-info",
        "/company/supply/report-for-detail-supplies-interact-info",
        "/user/supply/report-for-detail-supplies-interact-info"})
    public ModelAndView getReportForDetailSuppliesInteractInfoPage(HttpServletRequest request, Model model) throws SQLException {
        return authenticatedServices.getReportForDetailSuppliesInteractInfoPage(request, model);
    }

    /*_____________RequestMethod.POST: Supply-entity-interaction_____________*/
    @PostMapping({"${url.post.branch.prefix.v1}/find-supply-by-values",
        "${url.post.company.prefix.v1}/find-supply-by-values",
        "${url.post.user.prefix.v1}/find-supply-by-values"})
    public ResponseEntity<ResDtoRetrievingData<Supply>> findingSuppliesByValues(
        @RequestBody ReqDtoRetrievingData<Supply> searchingObject,
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticatedServices.findSupply(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }

    @PostMapping({"${url.post.branch.prefix.v1}/find-supply-by-values-for-order-detail",
        "${url.post.company.prefix.v1}/find-supply-by-values-for-order-detail",
        "${url.post.user.prefix.v1}/find-supply-by-values-for-order-detail"})
    public ResponseEntity<ResDtoRetrievingData<ResDtoSupplyForImportToBuildDialog>> findSupplyForOrderDetail(
        @RequestBody ReqDtoRetrievingData<ResDtoSupplyForImportToBuildDialog> searchingObject,
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticatedServices.findSupplyForOrderDetail(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }

    @PostMapping({"${url.post.branch.prefix.v1}/find-tickets-for-detail-supplies-report",
        "${url.post.company.prefix.v1}/find-tickets-for-detail-supplies-report",
        "${url.post.user.prefix.v1}/find-tickets-for-detail-supplies-report"})
    public ResponseEntity<ResDtoRetrievingData<ResDtoTicketsForDetailSuppliesReport>> findingTicketsForDetailSuppliesReport(
        @RequestBody ReqDtoTicketsForDetailSuppliesReport searchingObject,
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticatedServices.findTicketsForDetailSuppliesReport(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping({"${url.post.branch.prefix.v1}/add-supply",
        "${url.post.user.prefix.v1}/add-supply"})
    public String addSupply(
        @ModelAttribute("supply") Supply supply,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<Supply>> violations = hibernateValidator.validate(supply);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            redirectAttributes.addFlashAttribute("submittedSupply", supply);
            return "redirect:" + standingUrl;
        }

        try {
            authenticatedServices.addSupply(request, supply);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_add_01");
        } catch (DuplicateKeyException ignored) {
            redirectAttributes.addFlashAttribute("submittedSupply", supply);
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_04");
        } catch (SQLException ignored) {
            redirectAttributes.addFlashAttribute("submittedSupply", supply);
            redirectAttributes.addFlashAttribute("errorCode", "error_supply_01");
        } catch (Exception ignored) {
            redirectAttributes.addFlashAttribute("submittedSupply", supply);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping({"${url.post.branch.prefix.v1}/update-supply",
        "${url.post.user.prefix.v1}/update-supply"})
    public String updateSupply(
        @ModelAttribute("supply") Supply supply,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        try {
            authenticatedServices.updateSupply(supply, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_update_01");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_01");
            logger.info(e.toString());
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_supply_01");
            logger.info(e.toString());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
            logger.info(e.toString());
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping({"${url.post.branch.prefix.v1}/delete-supply",
        "${url.post.user.prefix.v1}/delete-supply"})
    public String deleteSupply(
        @RequestParam("deleteBtn") String supplyId,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        try {
            authenticatedServices.deleteSupply(supplyId, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_delete_01");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_01");
            logger.info(e.toString());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_supply_02");
            logger.info(e.toString());
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_02");
            logger.info(e.toString());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
            logger.info(e.toString());
        }
        return "redirect:" + standingUrl;
    }
}
