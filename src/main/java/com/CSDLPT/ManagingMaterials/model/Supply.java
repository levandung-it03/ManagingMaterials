package com.CSDLPT.ManagingMaterials.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
    @Length(min = 1, max = 30, message = "error_entity_03")
    @Pattern(regexp = "^[A-ZÀ-ỹ0-9]( [A-ZÀ-ỹ0-9])*$", message = "error_entity_03")
    private String supplyName;

    @NotBlank(message = "error_entity_03")
    @Length(min = 1, max = 15, message = "error_entity_03")
    private String unit;

    @NotNull(message = "error_entity_03")
    @Min(value = 0, message = "error_entity_03")
    private Integer quantityInStock;
}
