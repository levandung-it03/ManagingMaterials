package com.CSDLPT.ManagingMaterials.EN_OrderDetail.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqDtoDataForDetail {
    private String name;
    private String value;
}
