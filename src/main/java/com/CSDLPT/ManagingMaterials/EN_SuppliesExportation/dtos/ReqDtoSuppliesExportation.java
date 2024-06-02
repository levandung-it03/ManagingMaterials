package com.CSDLPT.ManagingMaterials.EN_SuppliesExportation.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqDtoSuppliesExportation {
    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,7}\\d{1,7}$", message = "error_entity_03")
    @Length(max = 8, message = "error_entity_03")
    private String suppliesExportationId;

    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ]{1,50}( [A-Za-zÀ-ỹ]{1,50})*$", message = "error_entity_03")
    @Length(max = 100, message = "error_entity_03")
    private String customerFullName;

    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,4}\\d{0,3}", message = "error_entity_03")
    @Length(max = 4, message = "error_entity_03")
    private String warehouseIdAsFk;

    public void trimAllFieldValues() {
        this.suppliesExportationId = this.getSuppliesExportationId().trim();
        this.customerFullName = this.getCustomerFullName().trim();
        this.warehouseIdAsFk = this.getWarehouseIdAsFk().trim();
    }
}
