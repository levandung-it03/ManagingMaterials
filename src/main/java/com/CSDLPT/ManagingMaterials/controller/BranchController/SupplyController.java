package com.CSDLPT.ManagingMaterials.controller.BranchController;

import com.CSDLPT.ManagingMaterials.dto.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.dto.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.model.Supply;
import com.CSDLPT.ManagingMaterials.service.BranchService.SupplyService;
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

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("${url.post.branch.prefix.v1}")
public class SupplyController {
    private final SupplyService supplyService;
    private final Validator hibernateValidator;
    private final Logger logger;

    @PostMapping("/find-supply-by-values")
    public ResponseEntity<ResDtoRetrievingData<Supply>> findingSuppliesByValues(
            @RequestBody ReqDtoRetrievingData<Supply> searchingObject,
            HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(supplyService.findSupply(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }

    @PostMapping("/add-supply")
    public String addSupply(
            @ModelAttribute("supply") Supply supply,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<Supply>> violations = hibernateValidator.validate(supply);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            return "redirect:" + standingUrl;
        }

        try {
            supplyService.addSupply(request, supply);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_add_01");
        } catch (DuplicateKeyException ignored) {
            redirectAttributes.addFlashAttribute("submittedSupply", supply);
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_04");
        } catch (Exception ignored) {
            redirectAttributes.addFlashAttribute("submittedSupply", supply);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + standingUrl;
    }

//    @PostMapping("/update-supply")
//    public String updateSupply(
//            @ModelAttribute("supply") Supply supply,
//            HttpServletRequest request,
//            RedirectAttributes redirectAttributes
//    ) {
//        final String standingUrl = request.getHeader("Referer");
//        try {
//            supplyService.updateSupply(supply, request);
//            redirectAttributes.addFlashAttribute("succeedCode", "succeed_update_01");
//        } catch (NumberFormatException e) {
//            redirectAttributes.addFlashAttribute("errorCode", "error_entity_01");
//            logger.info(e.toString());
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
//            logger.info(e.toString());
//        }
//        return "redirect:" + standingUrl;
//    }
//
//    @PostMapping("/delete-supply")
//    public String deleteSupply(
//            @RequestParam("deleteBtn") String supplyId,
//            HttpServletRequest request,
//            RedirectAttributes redirectAttributes
//    ) {
//        final String standingUrl = request.getHeader("Referer");
//        try {
//            supplyService.deleteSupply(supplyId, request);
//            redirectAttributes.addFlashAttribute("succeedCode", "succeed_delete_01");
//        } catch (NumberFormatException e) {
//            redirectAttributes.addFlashAttribute("errorCode", "error_entity_01");
//            logger.info(e.toString());
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
//            logger.info(e.toString());
//        }
//        return "redirect:" + standingUrl;
//    }
}
