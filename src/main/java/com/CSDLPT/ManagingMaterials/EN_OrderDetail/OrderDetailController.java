package com.CSDLPT.ManagingMaterials.EN_OrderDetail;

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
public class OrderDetailController {
    private final OrderDetailService.BranchServices branchServices;
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
                .body(branchServices.findOrderDetail(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }
}
