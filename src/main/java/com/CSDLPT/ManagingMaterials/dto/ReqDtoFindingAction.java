package com.CSDLPT.ManagingMaterials.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqDtoFindingAction <T> {
    private Class<T> objectType = null;
    private String searchingTable = "";
    private String moreCondition = "";
    private String sortingCondition = "";
    @NotBlank
    private String searchingField;
    @NotBlank
    private String searchingValue;
}
