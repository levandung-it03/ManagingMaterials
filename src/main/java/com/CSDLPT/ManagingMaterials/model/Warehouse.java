package com.CSDLPT.ManagingMaterials.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {
    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,4}$", message = "error_entity_03")
    private String warehouseId;

    @NotBlank(message = "error_entity_03")
    @Length(min = 1, max = 30, message = "error_entity_03")
    @Pattern(regexp = "^[A-ZÀ-ỹ0-9]( [A-ZÀ-ỹ0-9])*$", message = "error_entity_03")
    private String warehouseName;

    @NotBlank(message = "error_entity_03")
    @Length(min = 1, max = 100, message = "error_entity_03")
    private String address;

    @Pattern(regexp = "^[A-Z0-9]{1,10}$", message = "error_entity_03")
    private String branch;
}
