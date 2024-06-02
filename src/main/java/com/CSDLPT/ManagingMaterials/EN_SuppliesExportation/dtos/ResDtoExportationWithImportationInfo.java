package com.CSDLPT.ManagingMaterials.EN_SuppliesExportation.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResDtoExportationWithImportationInfo {
    private String suppliesExportationId;
    private String customerFullName;
    private Integer employeeIdAsFk;
    private String lastName;
    private String firstName;
    private String warehouseIdAsFk;
    private String warehouseName;
    private Date createdDate;
}
