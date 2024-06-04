package com.CSDLPT.ManagingMaterials.EN_Warehouse;

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
@RequestMapping("")
public class WarehouseController {
    private final WarehouseService.BranchServices branchServices;
    private final Logger logger;
    private final Validator hibernateValidator;

    /** Spring MVC: Branch-role controllers **/
    /*_____________RequestMethod.GET: Header-pages_____________*/
    @GetMapping("/branch/warehouse/manage-warehouse")
    public ModelAndView getManageWarehousePage(HttpServletRequest request, Model model) throws SQLException {
        return branchServices.getManageWarehousePage(request, model);
    }

    /*_____________RequestMethod.POST: Warehouse-entity-interaction_____________*/
    @PostMapping("${url.post.branch.prefix.v1}/find-warehouse-by-values")
    public ResponseEntity<ResDtoRetrievingData<Warehouse>> findingWarehousesByValues(
        @RequestBody ReqDtoRetrievingData<Warehouse> searchingObject,
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(branchServices.findWarehouse(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }

    @PostMapping("${url.post.branch.prefix.v1}/add-warehouse")
    public String addWarehouse(
        @ModelAttribute("warehouse") Warehouse warehouse,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<Warehouse>> violations = hibernateValidator.validate(warehouse);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            redirectAttributes.addFlashAttribute("submittedWarehouse", warehouse);
            return "redirect:" + standingUrl;
        }

        try {
            branchServices.addWarehouse(request, warehouse);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_add_01");
        } catch (DuplicateKeyException ignored) {
            redirectAttributes.addFlashAttribute("submittedWarehouse", warehouse);
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_04");
        } catch (SQLException ignored) {
            redirectAttributes.addFlashAttribute("submittedWarehouse", warehouse);
            redirectAttributes.addFlashAttribute("errorCode", "error_warehouse_01");
        } catch (Exception ignored) {
            redirectAttributes.addFlashAttribute("submittedWarehouse", warehouse);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping("${url.post.branch.prefix.v1}/update-warehouse")
    public String updateWarehouse(
        @ModelAttribute("warehouse") Warehouse warehouse,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        try {
            branchServices.updateWarehouse(warehouse, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_update_01");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_01");
            logger.info(e.toString());
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_warehouse_01");
            logger.info(e.toString());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
            logger.info(e.toString());
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping("${url.post.branch.prefix.v1}/delete-warehouse")
    public String deleteWarehouse(
        @RequestParam("deleteBtn") String warehouseId,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        try {
            branchServices.deleteWarehouse(warehouseId, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_delete_01");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_01");
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
