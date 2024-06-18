package com.CSDLPT.ManagingMaterials.EN_Order.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResDtoReportForOrderDontHaveImport {
    private String orderId;
    private Date createdDate;
    private String supplier;
    private String employeeFullName;
    private String supplyName;
    private String suppliesQuantity;
    private Double price;
}
