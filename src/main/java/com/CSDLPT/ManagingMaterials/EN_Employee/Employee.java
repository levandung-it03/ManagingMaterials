package com.CSDLPT.ManagingMaterials.EN_Employee;

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
public class Employee {
    @NotNull(message = "error_entity_03")
    @Min(value = 1, message = "error_entity_03")
    private Integer employeeId;

    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[0-9]{9,20}$", message = "error_entity_03")
    private String identifier;

    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ]{1,50}( [A-Za-zÀ-ỹ]{1,50})*$", message = "error_entity_03")
    private String lastName;

    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ]{1,50}$", message = "error_entity_03")
    private String firstName;

    @NotBlank(message = "error_entity_03")
    @Length(min = 1, max = 100, message = "error_entity_03")
    private String address;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    @NotNull(message = "error_entity_03")
    @Past(message = "error_entity_03")
    private Date birthday;

    @NotNull(message = "error_entity_03")
    @Min(value = 4000000, message = "error_entity_03")
    private Double salary;

    @Pattern(regexp = "^[A-Z0-9]{1,10}$", message = "error_entity_03")
    private String branch = null;

    @NotNull(message = "error_entity_03")
    @Min(value = 0, message = "error_entity_03")
    @Max(value = 1, message = "error_entity_03")
    private int deletedStatus = 0;
}
