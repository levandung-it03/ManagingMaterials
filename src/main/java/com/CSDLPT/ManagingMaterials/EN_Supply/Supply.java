package com.CSDLPT.ManagingMaterials.EN_Supply;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supply {
    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,3}\\d{1,3}$", message = "error_entity_03")
    private String supplyId;

    @NotBlank(message = "error_entity_03")
    @Length(min = 1, max = 30, message = "error_entity_03")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ]{1,50}( [A-Za-zÀ-ỹ0-9]{1,30})*$", message = "error_entity_03")
    private String supplyName;

    @NotBlank(message = "error_entity_03")
    @Length(min = 1, max = 15, message = "error_entity_03")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ]{1,15}$", message = "error_entity_03")
    private String unit;

    @NotNull(message = "error_entity_03")
    @Min(value = 0, message = "error_entity_03")
    private Integer quantityInStock;
}
