package com.CSDLPT.ManagingMaterials.EN_Supply.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResDtoSupplyForImportToBuildDialog {
    private String supplyId;
    private String supplyName;
    private String unit;
    private Integer suppliesQuantityFromOrderDetailAsFk;
}
