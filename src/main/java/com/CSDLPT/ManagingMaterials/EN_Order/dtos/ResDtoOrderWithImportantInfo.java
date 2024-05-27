package com.CSDLPT.ManagingMaterials.EN_Order.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResDtoOrderWithImportantInfo {
    private String orderId;
    private String supplier;
    private Integer employeeIdAsFk;
    private String lastName;
    private String firstName;
    private String warehouseIdAsFk;
    private String warehouseName;
    private Date createdDate;
}
