package com.CSDLPT.ManagingMaterials.service.BranchService;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.dto.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.dto.ReqDtoDataForDetail;
import com.CSDLPT.ManagingMaterials.dto.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.model.OrderDetail;
import com.CSDLPT.ManagingMaterials.service.GeneralService.FindingActionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final StaticUtilMethods staticUtilMethods;
    private final FindingActionService findingActionService;

    public ResDtoRetrievingData<OrderDetail> findOrderDetail(
        HttpServletRequest request,
        ReqDtoRetrievingData<OrderDetail> searchingObject
    ) throws SQLException, NoSuchFieldException {
        //--Preparing data to fetch.
        searchingObject.setObjectType(OrderDetail.class);
        searchingObject.setSearchingTable("CTDDH");
        searchingObject.setSearchingTableIdName("MasoDDH");
        searchingObject.setSortingCondition("ORDER BY MasoDDH ASC");

        StringBuilder condition = new StringBuilder();
        for (ReqDtoDataForDetail dataObject : searchingObject.getConditionObjectsList()) {
            //--Convert object name into field name in DB
            dataObject.setName(staticUtilMethods.columnNameStaticDictionary(dataObject.getName()).getFirst());
            //--Add more condition to condition variable
            condition.append(!condition.isEmpty() ? " AND " : "");
            condition.append(String.format("%s = '%s'", dataObject.getName(), dataObject.getValue()));
        }
        searchingObject.setMoreCondition(condition.toString());

        return findingActionService.findingDataAndServePaginationBarFormat(request, searchingObject);
    }
}
