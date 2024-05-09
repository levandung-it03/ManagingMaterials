package com.CSDLPT.ManagingMaterials.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqDtoRetrievingData<T> {
    //--Data from client
    @NotNull
    private Integer currentPage = 1;
    private String searchingField;
    private String searchingValue;
    private List<ReqDtoDataForDetail> conditionObjectsList;
    //--Data for customizing in Services.
    private Class<T> objectType = null;
    private String searchingTable = "";
    private String searchingTableIdName = "";
    private String moreCondition = "";
    private String sortingCondition = "";
}
