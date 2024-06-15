package com.CSDLPT.ManagingMaterials.EN_Employee.dtos;

import jakarta.validation.constraints.*;
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
public class ReqDtoReportForEmployeeActivities {
    @NotNull(message = "error_entity_03")
    @Min(value = 1, message = "error_entity_03")
    private Integer employeeId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "error_entity_03")
    @PastOrPresent(message = "error_entity_03")
    private Date startingDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "error_entity_03")
    @PastOrPresent(message = "error_entity_03")
    private Date endingDate;
}
