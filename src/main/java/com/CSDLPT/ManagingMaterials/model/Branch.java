package com.CSDLPT.ManagingMaterials.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Branch {
    private String branchId;
    private String branchName;
    private String branchAddress;
    private String branchPhoneNumber;
}
