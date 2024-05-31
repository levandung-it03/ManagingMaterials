package com.CSDLPT.ManagingMaterials.EN_SuppliesExportation;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class SuppliesExportation {
    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,7}\\d{1,7}$", message = "error_entity_03")
    @Length(max = 8, message = "error_entity_03")
    private String suppliesExportationId;

    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ]{1,50}( [A-Za-zÀ-ỹ]{1,50})*$", message = "error_entity_03")
    @Length(max = 100, message = "error_entity_03")
    private String customerFullName;

    @NotNull(message = "error_entity_03")
    private Integer employeeId;

    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z]{1,4}\\d{0,3}", message = "error_entity_03")
    @Length(max = 4, message = "error_entity_03")
    private String warehouseId;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    @NotNull(message = "error_entity_03")
    @FutureOrPresent(message = "error_entity_03")
    private Date createdDate;
}
