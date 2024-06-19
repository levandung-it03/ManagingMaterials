package com.CSDLPT.ManagingMaterials.EN_Reports.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
public class ImportAndExportStatisticDto {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "error_entity_03")
    @PastOrPresent(message = "error_entity_03")
    private Date startingDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "error_entity_03")
    @PastOrPresent(message = "error_entity_03")
    private Date endingDate;
}
