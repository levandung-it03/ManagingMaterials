package com.CSDLPT.ManagingMaterials.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {
    @NotNull(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,7}\\d{1,7}$", message = "error_entity_03")
    private String orderId;

    @NotNull(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,2}\\d{1,2}$", message = "error_entity_03")
    private String supplyId;

    @NotBlank(message = "error_entity_03")
    @Min(value = 0, message = "error_entity_03")
    private Integer quantitySupply;

    @NotNull(message = "error_entity_03")
    @Min(value = 0, message = "error_entity_03")
    private Double price;
}
