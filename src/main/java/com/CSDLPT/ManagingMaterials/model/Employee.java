package com.CSDLPT.ManagingMaterials.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Min(value = 1, message = "error_entity_03")
    private Integer employeeId;

    @NotEmpty(message = "error_entity_03")
    @Pattern(regexp = "^[0-9]{9,20}$", message = "error_entity_03")
    private String identifier;

    @NotEmpty(message = "error_entity_03")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ]{1,50}( [A-Za-zÀ-ỹ]{1,50})*$", message = "error_entity_03")
    private String lastName;

    @NotEmpty(message = "error_entity_03")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ]{1,50}$", message = "error_entity_03")
    private String firstName;

    private String address;

    @NotNull(message = "error_entity_03")
    @Past(message = "error_entity_03")
    private Date birthday;

    @NotNull(message = "error_entity_03")
    @Min(value = 0, message = "error_entity_03")
    @Max(value = 4000000, message = "error_entity_03")
    private Integer salary;

    private String branch = null;

    @NotEmpty(message = "error_entity_03")
    @Min(value = 0, message = "error_entity_03")
    @Max(value = 1, message = "error_entity_03")
    private int deletedStatus = 0;
}
