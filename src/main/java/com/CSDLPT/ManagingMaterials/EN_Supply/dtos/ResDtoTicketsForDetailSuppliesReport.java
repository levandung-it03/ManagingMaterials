package com.CSDLPT.ManagingMaterials.EN_Supply.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResDtoTicketsForDetailSuppliesReport {
    private String month;
    private String supplyName;
    private Integer totalSuppliesQuantity;
    private Double totalPrices;
}
