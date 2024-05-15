package com.CSDLPT.ManagingMaterials.EN_Branch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Branch {
    private String branch;
    private String branchName;
    private String branchAddress;
    private String branchPhoneNumber;
}
