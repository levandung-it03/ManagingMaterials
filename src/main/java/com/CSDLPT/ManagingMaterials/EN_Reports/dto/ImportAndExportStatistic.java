package com.CSDLPT.ManagingMaterials.EN_Reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportAndExportStatistic {
    private Date date;
    private Integer supplyImport;
    private Double importPercentage;
    private Integer supplyExport;
    private Double exportPercentage;
}
