package com.CSDLPT.ManagingMaterials.EN_Order;

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
public class Order {
    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,7}\\d{1,7}$", message = "error_entity_03")
    @Length(max = 8, message = "error_entity_03")
    private String orderId;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    @NotNull(message = "error_entity_03")
    @FutureOrPresent(message = "error_entity_03")
    private Date createdDate;

    @NotBlank(message = "error_entity_03")
    @Length(min = 1, max = 100, message = "error_entity_03")
    private String supplier;

    @NotNull(message = "error_entity_03")
    private Integer employeeId;

    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,4}\\d{0,3}", message = "error_entity_03")
    @Length(max = 4, message = "error_entity_03")
    private String warehouseId;
}
