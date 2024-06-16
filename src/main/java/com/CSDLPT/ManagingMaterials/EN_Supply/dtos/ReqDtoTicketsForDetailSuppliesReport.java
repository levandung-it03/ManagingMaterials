package com.CSDLPT.ManagingMaterials.EN_Supply.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqDtoTicketsForDetailSuppliesReport {
    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^(NHAP|XUAT)$", message = "error_entity_03")
    private String ticketsType;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "error_entity_03")
    @PastOrPresent(message = "error_entity_03")
    private Date startingDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "error_entity_03")
    @PastOrPresent(message = "error_entity_03")
    private Date endingDate;
}
