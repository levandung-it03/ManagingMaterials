package com.CSDLPT.ManagingMaterials.service.BranchService;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.dto.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.dto.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.dto.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.model.Employee;
import com.CSDLPT.ManagingMaterials.model.PageObject;
import com.CSDLPT.ManagingMaterials.model.Warehouse;
import com.CSDLPT.ManagingMaterials.repository.WarehouseRepository;
import com.CSDLPT.ManagingMaterials.service.GeneralService.FindingActionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    public ModelAndView getManageWarehousePage(HttpServletRequest request, Model model) {
        //--Prepare a modelAndView object to symbolize the whole page.
        ModelAndView modelAndView = staticUtilMethods
            .customResponseModelView(request, model.asMap(), "manage-warehouse");

        //--Check if there's a response warehouse to map into adding-form when an error occurred.
        Warehouse warehouse = (Warehouse) model.asMap().get("submittedWarehouse");
        if (warehouse != null)   modelAndView.addObject("warehouse", warehouse);

        return modelAndView;
    }

    public ResDtoRetrievingData<Warehouse> findWarehouse(
        HttpServletRequest request,
        ReqDtoRetrievingData<Warehouse> searchingObject
    ) throws SQLException {
        //--Preparing data to fetch.
        searchingObject.setObjectType(Warehouse.class);
        searchingObject.setSearchingTable("KHO");
        searchingObject.setSearchingTableIdName("MAKHO");
        searchingObject.setSortingCondition("ORDER BY MAKHO ASC");

        return findingActionService.findingDataAndServePaginationBarFormat(request, searchingObject);
    }

    public void addWarehouse(HttpServletRequest request, Warehouse warehouse) throws SQLException {
        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        //--Prepare data to save.
        ResDtoUserInfo userInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");
        warehouse.setBranch(userInfo.getBranch());

        warehouseRepository.save(connectionHolder, warehouse);

        //--Close Connection.
        connectionHolder.removeConnection();
    }
}
