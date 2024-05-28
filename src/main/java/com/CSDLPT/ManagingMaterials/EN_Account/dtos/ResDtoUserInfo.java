package com.CSDLPT.ManagingMaterials.EN_Account.dtos;

import com.CSDLPT.ManagingMaterials.EN_Account.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResDtoUserInfo {
    private String username;
    private String password;
    private String branch;
    private Integer employeeId;
    private String fullName;
    private RoleEnum role;
}
