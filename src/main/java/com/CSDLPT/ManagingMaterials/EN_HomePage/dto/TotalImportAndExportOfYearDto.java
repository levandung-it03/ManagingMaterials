package com.CSDLPT.ManagingMaterials.EN_HomePage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalImportAndExportOfYearDto {
    Integer year;
    private List<Integer> monthlyImport;
    private List<Integer> monthlyExport;
}
