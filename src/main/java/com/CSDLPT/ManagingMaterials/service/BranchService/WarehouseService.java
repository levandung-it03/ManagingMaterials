package com.CSDLPT.ManagingMaterials.service.BranchService;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.dto.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.model.PageObject;
import com.CSDLPT.ManagingMaterials.model.Warehouse;
import com.CSDLPT.ManagingMaterials.repository.WarehouseRepository;
import com.CSDLPT.ManagingMaterials.service.GeneralService.FindingActionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final StaticUtilMethods staticUtilMethods;
    private final WarehouseRepository warehouseRepository;
    private final FindingActionService findingActionService;

    public ModelAndView getManageWarehousePage(HttpServletRequest request, Model model) throws SQLException {
        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        //--Prepare a modelAndView object to symbolize the whole page.
        ModelAndView modelAndView = staticUtilMethods.customResponseModelView(request, model.asMap(), "manage-warehouse");

        //--Check if there's a response warehouse to map into adding-form when an error occurred.
        Warehouse warehouse = (Warehouse) model.asMap().get("warehouse");
        if (warehouse != null)   modelAndView.addObject("warehouse", warehouse);

        //--Get pageObj for paging the list.
        //--Get warehousesList data for adding-form.
        List<Warehouse> warehousesList = warehouseRepository.findAll(connectionHolder, new PageObject(request));
        modelAndView.addObject("warehousesList", warehousesList);

        //--Close Connection.
        connectionHolder.removeConnection();

        return modelAndView;
    }

    public List<Warehouse> findWarehouse(HttpServletRequest request, ReqDtoRetrievingData<Warehouse> searchingObject) {
//        searchingObject.setObjectType(Warehouse.class);
//        searchingObject.setSearchingTable("KHO");
//        searchingObject.setSortingCondition("ORDER BY MAKHO ASC");
//        return findingActionService.findingDataWithPaging(request, searchingObject);
        return List.of();
    }
}
