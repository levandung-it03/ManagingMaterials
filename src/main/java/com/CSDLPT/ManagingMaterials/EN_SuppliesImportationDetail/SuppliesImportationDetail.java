package com.CSDLPT.ManagingMaterials.EN_SuppliesImportationDetail;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class SuppliesImportationDetail {
    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,7}\\d{1,7}$", message = "error_entity_03")
    @Length(max = 8, message = "error_entity_03")
    private String suppliesImportationId;

    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,4}\\d{0,3}", message = "error_entity_03")
    @Length(max = 4, message = "error_entity_03")
    private String supplyId;

    @NotNull(message = "error_entity_03")
    @Min(value = 1, message = "error_entity_03")
    private Integer suppliesQuantity;

    @NotNull(message = "error_entity_03")
    @Min(value = 0, message = "error_entity_03")
    private Double price;

    public void trimAllFieldValues() {
        this.suppliesImportationId = this.getSuppliesImportationId().trim();
        this.supplyId = this.getSupplyId().trim();
    }
}
