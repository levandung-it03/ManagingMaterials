package com.CSDLPT.ManagingMaterials.EN_OrderDetail;

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
public class OrderDetailController {
    private final OrderDetailService.BranchServices branchServices;
    private final Validator hibernateValidator;
    private final Logger logger;

    /** Spring MVC: Branch-role controllers **/
    /*_____________RequestMethod.GET: Header-pages_____________*/
    @GetMapping("/branch/order-detail/manage-order-detail")
    public ModelAndView getManageOrderDetailPage(HttpServletRequest request, Model model) {
        return branchServices.getManageOrderDetailPage(request, model);
    }

    /*_____________RequestMethod.POST: OrderDetail-entity-interaction_____________*/
    @PostMapping("${url.post.branch.prefix.v1}/find-order-detail-by-values")
    public ResponseEntity<ResDtoRetrievingData<OrderDetail>> findingOrderDetailsByValues(
        @RequestBody ReqDtoRetrievingData<OrderDetail> searchingObject,
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(branchServices.findingOrderDetail(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }

    @PostMapping("${url.post.branch.prefix.v1}/add-order-detail")
    public String addOrderDetail(
        @ModelAttribute("orderDetail") OrderDetail orderDetail,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<OrderDetail>> violations = hibernateValidator.validate(orderDetail);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            redirectAttributes.addFlashAttribute("submittedOrderDetail", orderDetail);
            return "redirect:" + standingUrl;
        }

        try {
            branchServices.addOrderDetail(orderDetail, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_add_01");
        } catch (DuplicateKeyException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_supply_01");
            redirectAttributes.addFlashAttribute("submittedOrderDetail", orderDetail);
        } catch (Exception e) {
            logger.info("Error from AddOrderDetailController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
            redirectAttributes.addFlashAttribute("submittedOrderDetail", orderDetail);
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping("${url.post.branch.prefix.v1}/update-order-detail")
    public String updateOrderDetail(
        @ModelAttribute("orderDetail") OrderDetail orderDetail,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<OrderDetail>> violations = hibernateValidator.validate(orderDetail);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            redirectAttributes.addFlashAttribute("submittedOrderDetail", orderDetail);
            return "redirect:" + standingUrl;
        }

        try {
            branchServices.updateOrderDetail(orderDetail, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_update_01");
        } catch (Exception e) {
            logger.info("Error from UpdateOrderDetailController: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
            redirectAttributes.addFlashAttribute("submittedOrderDetail", orderDetail);
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping("${url.post.branch.prefix.v1}/delete-order-detail")
    public String deleteOrderDetail(
        @RequestParam("deleteBtn") String orderDetailId,
        @RequestParam("supplyId") String supplyId,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        try {
            branchServices.deleteOrderDetail(orderDetailId, supplyId, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_delete_01");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_01");
        } catch (Exception e) {
            logger.info("Error from deleteOrderDetail: " + e);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + request.getHeader("Referer");
    }
}
