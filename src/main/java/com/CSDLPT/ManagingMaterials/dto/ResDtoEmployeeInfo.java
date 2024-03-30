package com.CSDLPT.ManagingMaterials.dto;

import com.CSDLPT.ManagingMaterials.model.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResDtoEmployeeInfo {
    private String username;
    private String password;
    private String branch;
    private String employeeId;
    private String fullName;
    private Role role;
}
