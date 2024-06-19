package com.CSDLPT.ManagingMaterials.EN_SuppliesImportation;

import com.CSDLPT.ManagingMaterials.EN_Account.RoleEnum;
import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.EN_Branch.BranchRepository;
import com.CSDLPT.ManagingMaterials.EN_Order.OrderRepository;
import com.CSDLPT.ManagingMaterials.EN_SuppliesImportation.dtos.ReqDtoSuppliesImportation;
import com.CSDLPT.ManagingMaterials.EN_SuppliesImportation.dtos.ResDtoImportationWithImportationInfo;
import com.CSDLPT.ManagingMaterials.EN_SuppliesImportationDetail.SuppliesImportationDetailRepository;
import com.CSDLPT.ManagingMaterials.EN_Warehouse.WarehouseRepository;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.FindingActionService;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.InnerJoinObject;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class SuppliesImportationService {
    @Service
    @RequiredArgsConstructor
    public static class AuthenticatedServices {
        @Value("${mssql.database.name}")
        private String databaseName;
        private final StaticUtilMethods staticUtilMethods;
        private final FindingActionService findingActionService;
        private final SuppliesImportationRepository suppliesImportationRepository;
        private final SuppliesImportationDetailRepository suppliesImportationDetailRepository;
        private final WarehouseRepository warehouseRepository;
        private final OrderRepository orderRepository;
        private final BranchRepository branchRepository;

        public ModelAndView getManageSuppliesImportationPage(HttpServletRequest request, Model model) throws SQLException {
            //--Prepare a modelAndView object to symbolize the whole page.
            ModelAndView modelAndView = staticUtilMethods
                .customResponsiveModelView(request, model, "manage-supplies-importation");

            //--Check if there's a response SuppliesImportation to map into adding-form when an error occurred.
            ReqDtoSuppliesImportation importation = (ReqDtoSuppliesImportation) model.asMap().get("submittedSuppliesImportation");
            if (importation != null) modelAndView.addObject("suppliesImportation", importation);

            //--Prepare branches-list for several pages
            DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            modelAndView.addObject("branchesList", branchRepository.findAllBranchIds(connectionHolder));;
            connectionHolder.removeConnection();

            return modelAndView;
        }

        public ResDtoRetrievingData<ResDtoImportationWithImportationInfo> findSuppliesImportation(
            HttpServletRequest request,
            ReqDtoRetrievingData<ResDtoImportationWithImportationInfo> searchingObject
        ) throws SQLException, NoSuchFieldException {
            //--Preparing data to fetch.
            searchingObject.setObjectType(ResDtoImportationWithImportationInfo.class);
            searchingObject.setSearchingTable("PhieuNhap");
            searchingObject.setSearchingTableIdName("MAPN");
            searchingObject.setSortingCondition("ORDER BY MAPN ASC");

            ResDtoUserInfo userInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");
            List<InnerJoinObject> joinObjects = List.of(
                InnerJoinObject.builder()
                    .databaseName(databaseName).left("PhieuNhap").right("NhanVien").fields("MANV, HO, TEN").bridge("MANV")
                    .build(),
                InnerJoinObject.builder()
                    .databaseName(databaseName).left("PhieuNhap").right("Kho").fields("MAKHO, TENKHO").bridge("MAKHO")
                    .build()
            );
            if (userInfo.getRole().equals(RoleEnum.CONGTY))
                if (!searchingObject.getBranch().isEmpty() && !userInfo.getBranch().equals(searchingObject.getBranch()))
                    joinObjects.forEach(obj -> obj.setDifferentBranch(true));
            searchingObject.setJoiningCondition(InnerJoinObject.mergeQuery(joinObjects));
            return findingActionService.findingDataAndServePaginationBarFormat(request, searchingObject);
        }

        public void addSuppliesImportation(ReqDtoSuppliesImportation importation, HttpServletRequest request) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            importation.trimAllFieldValues();

            if (suppliesImportationRepository
                .isExistingSuppliesImportationBySuppliesImportationId(connectHolder, importation.getSuppliesImportationId()))
                throw new DuplicateKeyException("error_suppliesImportation_01");

            if (!warehouseRepository.isExistingWarehouseByWarehouseId(connectHolder, importation.getWarehouseIdAsFk()))
                throw new NoSuchElementException("error_warehouse_02");

            if (orderRepository.findById(connectHolder, importation.getOrderId()).isEmpty())
                throw new NoSuchElementException("error_order_01");

            if (suppliesImportationRepository.findByOrderId(connectHolder, importation.getOrderId()).isPresent())
                throw new DuplicateKeyException("error_order_02");

            ResDtoUserInfo currentUserInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");
            //--May throw SQLException if id is already existing.
            suppliesImportationRepository.save(connectHolder, SuppliesImportation.builder()
                .suppliesImportationId(importation.getSuppliesImportationId())
                .warehouseId(importation.getWarehouseIdAsFk())
                .orderId(importation.getOrderId())
                .employeeId(currentUserInfo.getEmployeeId())
                .createdDate(new Date())
                .build()
            );

            //--Close connection
            connectHolder.removeConnection();
        }

        public void updateSuppliesImportation(ReqDtoSuppliesImportation importation, HttpServletRequest request) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            importation.trimAllFieldValues();

            if (!warehouseRepository.isExistingWarehouseByWarehouseId(connectHolder, importation.getWarehouseIdAsFk()))
                throw new NoSuchElementException("error_warehouse_02");

            if (orderRepository.findById(connectHolder, importation.getOrderId()).isEmpty())
                throw new NoSuchElementException("error_order_01");

            if (suppliesImportationRepository.findByOrderIdToServeUpdate(
                connectHolder, importation.getSuppliesImportationId(), importation.getOrderId()
            ).isPresent())
                throw new DuplicateKeyException("error_order_02");

            if (suppliesImportationDetailRepository
                .existBySuppliesImportationId(connectHolder, importation.getSuppliesImportationId()))
                throw new NoSuchElementException("error_suppliesImportation_04");

            SuppliesImportation updatedImportation = suppliesImportationRepository
                .findById(connectHolder, importation.getSuppliesImportationId())
                .orElseThrow(() -> new NoSuchElementException("error_suppliesImportation_02"));

            updatedImportation.setWarehouseId(importation.getWarehouseIdAsFk());
            updatedImportation.setOrderId(importation.getOrderId());
            suppliesImportationRepository.updateById(connectHolder, updatedImportation);

            //--Close connection
            connectHolder.removeConnection();
        }

        public void deleteSuppliesImportation(String importationId, HttpServletRequest request) throws SQLException {
            DBConnectionHolder connectHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            if (suppliesImportationRepository.findById(connectHolder, importationId).isEmpty())
                throw new NoSuchElementException("error_suppliesImportation_02");

            if (suppliesImportationDetailRepository.existBySuppliesImportationId(connectHolder, importationId))
                throw new NoSuchElementException("error_suppliesImportation_04");

            suppliesImportationRepository.deleteById(connectHolder, importationId);

            //--Close connection
            connectHolder.removeConnection();
        }
    }

    @Service
    public static class BranchServices {

    }

    @Service
    public static class CompanyServices {

    }

    @Service
    public static class UserServices {

    }
}
