package com.CSDLPT.ManagingMaterials.controller.BranchController;

import com.CSDLPT.ManagingMaterials.dto.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.dto.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.model.OrderDetail;
import com.CSDLPT.ManagingMaterials.service.BranchService.OrderDetailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("${url.post.branch.prefix.v1}")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final Logger logger;
    @PostMapping("/find-order-detail-by-values")
    public ResponseEntity<ResDtoRetrievingData<OrderDetail>> findingOrderDetailsByValues(
            @RequestBody ReqDtoRetrievingData<OrderDetail> searchingObject,
            HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(orderDetailService.findOrderDetail(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }
}
