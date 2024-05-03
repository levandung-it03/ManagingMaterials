package com.CSDLPT.ManagingMaterials.controller.BranchController;

import com.CSDLPT.ManagingMaterials.dto.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.dto.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.model.Warehouse;
import com.CSDLPT.ManagingMaterials.service.BranchService.WarehouseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("${url.post.branch.prefix.v1}")
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final Logger logger;
    private final Validator hibernateValidator;

    @PostMapping("/find-warehouse-by-values")
    public ResponseEntity<ResDtoRetrievingData<Warehouse>> findingWarehousesByValues(
        @RequestBody ReqDtoRetrievingData<Warehouse> searchingObject,
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(warehouseService.findWarehouse(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }

    @PostMapping("/add-warehouse")
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
            warehouseService.addWarehouse(request, warehouse);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_add_01");
        } catch (DuplicateKeyException ignored) {
            redirectAttributes.addFlashAttribute("submittedWarehouse", warehouse);
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_04");
        } catch (SQLException ignored) {
            redirectAttributes.addFlashAttribute("submittedWarehouse", warehouse);
            redirectAttributes.addFlashAttribute("errorCode", "error_warehouse_01");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("submittedWarehouse", warehouse);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping("/update-warehouse")
    public String updateWarehouse(
        @ModelAttribute("warehouse") Warehouse warehouse,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        try {
            warehouseService.updateWarehouse(warehouse, request);
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

    @PostMapping("/delete-warehouse")
    public String deleteWarehouse(
        @RequestParam("deleteBtn") String warehouseId,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        try {
            warehouseService.deleteWarehouse(warehouseId, request);
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
