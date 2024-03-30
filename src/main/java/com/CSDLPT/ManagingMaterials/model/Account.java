package com.CSDLPT.ManagingMaterials.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @NotEmpty(message = "error_entity_03")
    private String branch;

    @NotEmpty(message = "error_account_01")
    private String username;

    @NotEmpty(message = "error_account_01")
    private String password;
}
