package com.CSDLPT.ManagingMaterials.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqDtoRetrievingData<T> {
    private Class<T> objectType = null;
    private String searchingTable = "";
    private String searchingTableIdName = "";
    private String moreCondition = "";
    private String sortingCondition = "";
    private String searchingField;
    private String searchingValue;
    @NotNull
    private Integer page = 1;
}
