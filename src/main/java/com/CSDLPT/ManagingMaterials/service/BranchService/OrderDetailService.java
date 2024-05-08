package com.CSDLPT.ManagingMaterials.service.BranchService;

import com.CSDLPT.ManagingMaterials.dto.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.dto.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.model.Order;
import com.CSDLPT.ManagingMaterials.model.OrderDetail;
import com.CSDLPT.ManagingMaterials.service.GeneralService.FindingActionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
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
        searchingObject.setMoreCondition(searchingObject.getMoreCondition());

        return findingActionService.findingDataAndServePaginationBarFormat(request, searchingObject);
    }
}
