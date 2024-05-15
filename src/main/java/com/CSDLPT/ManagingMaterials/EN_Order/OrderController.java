package com.CSDLPT.ManagingMaterials.EN_Order;

import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService.BranchServices branchServices;
    private final Logger logger;

    /** Spring MVC: Branch-role controllers **/
    /*_____________RequestMethod.GET: Header-pages_____________*/
    @GetMapping("/branch/order/manage-order")
    public ModelAndView getManageOrderPage(HttpServletRequest request, Model model) {
        return branchServices.getManageOrderPage(request, model);
    }

    /*_____________RequestMethod.POST: Order-entity-interaction_____________*/
    @PostMapping("${url.post.branch.prefix.v1}/find-order-by-values")
    public ResponseEntity<ResDtoRetrievingData<Order>> findingOrdersByValues(
        @RequestBody ReqDtoRetrievingData<Order> searchingObject,
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
}
