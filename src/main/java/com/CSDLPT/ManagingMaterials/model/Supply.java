package com.CSDLPT.ManagingMaterials.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supply {
    @NotNull(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z0-9]{1,10}$", message = "error_entity_03")
    private String supplyId;

    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ]{1,50}( [A-Za-zÀ-ỹ]{1,50})*$", message = "error_entity_03")
    private String supplyName;

    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[0-9]{9,20}$", message = "error_entity_03")
    private String unit;

    @NotNull(message = "error_entity_03")
    @Min(value = 0, message = "error_entity_03")
    private Integer quantityInStock;
}
