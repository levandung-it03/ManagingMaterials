package com.CSDLPT.ManagingMaterials.EN_Order;

import com.CSDLPT.ManagingMaterials.EN_Order.dtos.ReqDtoOrder;
import com.CSDLPT.ManagingMaterials.EN_Order.dtos.ResDtoOrderWithImportantInfo;
import com.CSDLPT.ManagingMaterials.EN_SuppliesExportation.dtos.ReqDtoSuppliesExportation;
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
public class OrderController {
    private final OrderService.BranchServices branchServices;
    private final Validator hibernateValidator;
    private final Logger logger;

    /** Spring MVC: Branch-role controllers **/
    /*_____________RequestMethod.GET: Header-pages_____________*/
    @GetMapping("/branch/order/manage-order")
    public ModelAndView getManageOrderPage(HttpServletRequest request, Model model) {
        return branchServices.getManageOrderPage(request, model);
    }

    /*_____________RequestMethod.POST: Order-entity-interaction_____________*/
    @PostMapping("${url.post.branch.prefix.v1}/find-order-by-values")
    public ResponseEntity<ResDtoRetrievingData<ResDtoOrderWithImportantInfo>> findingOrdersByValues(
        @RequestBody ReqDtoRetrievingData<ResDtoOrderWithImportantInfo> searchingObject,
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(branchServices.findOrder(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }
    @PostMapping("${url.post.branch.prefix.v1}/find-order-for-supplies-importation-by-values")
    public ResponseEntity<ResDtoRetrievingData<ResDtoOrderWithImportantInfo>> findOrderToServeSuppliesImportation(
        @RequestBody ReqDtoRetrievingData<ResDtoOrderWithImportantInfo> searchingObject,
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(branchServices.findOrderToServeSuppliesImportation(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }

    @PostMapping("${url.post.branch.prefix.v1}/add-order")
    public String addOrder(
        @ModelAttribute("order") ReqDtoOrder order,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<ReqDtoOrder>> violations = hibernateValidator.validate(order);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            redirectAttributes.addFlashAttribute("submittedOrder", order);
            return "redirect:" + standingUrl;
        }

        try {
            branchServices.addOrder(order, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_add_01");
        } catch (NoSuchElementException | DuplicateKeyException e) {
            redirectAttributes.addFlashAttribute("errorCode", e.getMessage());
            redirectAttributes.addFlashAttribute("submittedOrder", order);
        } catch (Exception e) {
            logger.info("Error from AddOrderController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
            redirectAttributes.addFlashAttribute("submittedOrder", order);
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping("${url.post.branch.prefix.v1}/update-order")
    public String updateOrder(
        @ModelAttribute("order") ReqDtoOrder order,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<ReqDtoOrder>> violations = hibernateValidator.validate(order);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            return "redirect:" + standingUrl;
        }

        try {
            branchServices.updateOrder(order, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_update_01");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorCode", e.getMessage());
        } catch (Exception e) {
            logger.info("Error from UpdateOrderController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping("${url.post.branch.prefix.v1}/delete-order")
    public String deleteOrder(
        @RequestParam("deleteBtn") String orderId,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        try {
            branchServices.deleteOrder(orderId, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_delete_01");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_order_01");
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_order_04");
        } catch (Exception e) {
            logger.info("Error from DeleteSuppliesExportationController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + request.getHeader("Referer");
    }
}
