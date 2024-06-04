package com.CSDLPT.ManagingMaterials.EN_Order.dtos;

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
public class ReqDtoOrder {
    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,7}\\d{1,7}$", message = "error_entity_03")
    @Length(max = 8, message = "error_entity_03")
    private String orderId;

    @NotBlank(message = "error_entity_03")
    @Length(min = 1, max = 100, message = "error_entity_03")
    private String supplier;

    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,4}\\d{0,3}", message = "error_entity_03")
    @Length(max = 4, message = "error_entity_03")
    private String warehouseIdAsFk;

    public void trimAllFieldValues() {
        this.orderId = this.getOrderId().trim();
        this.supplier = this.getSupplier().trim();
        this.warehouseIdAsFk = this.getWarehouseIdAsFk().trim();
    }
}
