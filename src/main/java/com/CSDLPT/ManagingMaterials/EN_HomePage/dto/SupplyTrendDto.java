package com.CSDLPT.ManagingMaterials.EN_HomePage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyTrendDto {
    private List<String> supplies;
    private List<Integer> supplyTotalImport;
    private List<Integer> supplyTotalExport;
}
