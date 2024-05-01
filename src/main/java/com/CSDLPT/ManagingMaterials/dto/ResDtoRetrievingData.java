package com.CSDLPT.ManagingMaterials.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ResDtoRetrievingData <T> {
    private Integer totalObjectsQuantityResult;
    private List<T> resultDataSet;
}
