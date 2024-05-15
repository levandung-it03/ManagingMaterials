package com.CSDLPT.ManagingMaterials.EN_Warehouse;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.FindingActionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

public class WarehouseService {

    @Service
    @RequiredArgsConstructor
    public static class BranchServices {
        private final StaticUtilMethods staticUtilMethods;
        private final WarehouseRepository warehouseRepository;
        private final FindingActionService findingActionService;

        public ModelAndView getManageWarehousePage(HttpServletRequest request, Model model) {
            //--Prepare a modelAndView object to symbolize the whole page.
            ModelAndView modelAndView = staticUtilMethods
                .customResponsiveModelView(request, model, "manage-warehouse");

            //--Check if there's a response warehouse to map into adding-form when an error occurred.
            Warehouse warehouse = (Warehouse) model.asMap().get("submittedWarehouse");
            if (warehouse != null)   modelAndView.addObject("warehouse", warehouse);

            return modelAndView;
        }

        public ResDtoRetrievingData<Warehouse> findWarehouse(
            HttpServletRequest request,
            ReqDtoRetrievingData<Warehouse> searchingObject
        ) throws SQLException, NoSuchFieldException {
            //--Preparing data to fetch.
            searchingObject.setObjectType(Warehouse.class);
            searchingObject.setSearchingTable("KHO");
            searchingObject.setSearchingTableIdName("MAKHO");
            searchingObject.setSortingCondition("ORDER BY MAKHO ASC");

            return findingActionService.findingDataAndServePaginationBarFormat(request, searchingObject);
        }

        public void addWarehouse(HttpServletRequest request, Warehouse warehouse) throws SQLException, DuplicateKeyException {
            //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
            DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            //--Prepare data to save.
            ResDtoUserInfo userInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");
            warehouse.setBranch(userInfo.getBranch());

            //--Check If 'MAKHO' is already existing or not.
            if (warehouseRepository.isExistingWarehouseByWarehouseId(connectionHolder, warehouse.getWarehouseId()))
                throw new DuplicateKeyException("Can't add an existing warehouse!");

            if (warehouseRepository.save(connectionHolder, warehouse) == 0)
                throw new SQLException("Something wrong with SQL");

            //--Close Connection.
            connectionHolder.removeConnection();
        }

        public void updateWarehouse(Warehouse warehouse, HttpServletRequest request) throws SQLException {
            //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
            DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            //--Check If 'MAKHO' is already existing or not.
            if (!warehouseRepository.isExistingWarehouseByWarehouseId(connectionHolder, warehouse.getWarehouseId()))
                throw new DuplicateKeyException("Updated Warehouse Id not found!");

            if (warehouseRepository.update(connectionHolder, warehouse) == 0)
                throw new SQLException("There's an error with SQL Server!");

            //--Close Connection.
            connectionHolder.removeConnection();
        }

        public void deleteWarehouse(String warehouseId, HttpServletRequest request) throws SQLException {
            //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
            DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            //--Check If 'MAKHO' is already existing or not.
            if (!warehouseRepository.isExistingWarehouseByWarehouseId(connectionHolder, warehouseId))
                throw new DuplicateKeyException("Deleted Warehouse Id not found!");

            if (warehouseRepository.delete(connectionHolder, warehouseId) == 0)
                throw new SQLException("Something wrong happened in your DBMS");
        }
    }

    @Service
    public static class CompanyServices {

    }

    @Service
    public static class UserServices {

    }
}
