package com.CSDLPT.ManagingMaterials.EN_OrderDetail;

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
public class OrderDetail {
    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,7}\\d{1,7}", message = "error_entity_03")
    @Length(max = 4, message = "error_entity_03")
    private String orderId;

    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,4}\\d{0,3}", message = "error_entity_03")
    @Length(max = 4, message = "error_entity_03")
    private String supplyId;

    @NotNull(message = "error_entity_03")
    @Min(value = 0, message = "error_entity_03")
    private Integer suppliesQuantity;

    @NotNull(message = "error_entity_03")
    @Min(value = 0, message = "error_entity_03")
    private Double price;
}
