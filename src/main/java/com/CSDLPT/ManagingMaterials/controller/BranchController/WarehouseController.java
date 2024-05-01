package com.CSDLPT.ManagingMaterials.controller.BranchController;

import com.CSDLPT.ManagingMaterials.dto.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.model.Warehouse;
import com.CSDLPT.ManagingMaterials.service.BranchService.WarehouseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("${url.post.branch.prefix.v1}")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PostMapping("/find-warehouse-by-values")
    public ResponseEntity<List<Warehouse>> findingWarehousesByValues(
        @RequestBody ReqDtoRetrievingData<Warehouse> searchingObject,
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(warehouseService.findWarehouse(request, searchingObject));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(List.of());
        }
    }
}
