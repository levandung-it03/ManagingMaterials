package com.CSDLPT.ManagingMaterials.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @NotBlank(message = "error_entity_03")
    @Pattern(regexp = "^[A-Z0-9]{1,10}$", message = "error_entity_03")
    private String branch;

    @NotBlank(message = "error_account_01")
    @Pattern(regexp = "^[A-Z]{1,10}$", message = "error_entity_03")
    private String username;

    @NotBlank(message = "error_account_01")
    private String password;
}
