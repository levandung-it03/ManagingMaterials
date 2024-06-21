package com.CSDLPT.ManagingMaterials.EN_Account.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ReqDtoAddingAccount {
    @NotNull(message = "error_entity_03")
    @Min(value = 1, message = "error_entity_03")
    private Integer employeeId;

    @Pattern(regexp = "^(CHINHANH|USER|CONGTY)$", message = "error_entity_03")
    private String role;

    @NotBlank(message = "error_account_01")
    @Pattern(regexp = "^[A-Z]{1,10}$", message = "error_entity_03")
    private String username;

    @Length(min = 8, message = "error_entity_03")
    private String password;

    @Length(min = 8, message = "error_entity_03")
    private String retypePassword;
}
