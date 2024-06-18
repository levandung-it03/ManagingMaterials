package com.CSDLPT.ManagingMaterials.EN_Supply;

import com.CSDLPT.ManagingMaterials.EN_Branch.BranchRepository;
import com.CSDLPT.ManagingMaterials.EN_Supply.dtos.ReqDtoTicketsForDetailSuppliesReport;
import com.CSDLPT.ManagingMaterials.EN_Supply.dtos.ResDtoSupplyForImportToBuildDialog;
import com.CSDLPT.ManagingMaterials.EN_Supply.dtos.ResDtoTicketsForDetailSuppliesReport;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.InnerJoinObject;
import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.FindingActionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public class SupplyService {
    @Service
    @RequiredArgsConstructor
    public static class BranchServices {
        private final StaticUtilMethods staticUtilMethods;
        private final SupplyRepository supplyRepository;
        private final FindingActionService findingActionService;
        private final BranchRepository branchRepository;

        public ModelAndView getManageSupplyPage(HttpServletRequest request, Model model) throws SQLException {
            //--Prepare common-components of ModelAndView if we need.
            ModelAndView modelAndView = staticUtilMethods
                .customResponsiveModelView(request, model, "manage-supply");

            //--If there's an error when handle data with DB, take the submitted-supply-info and give it back to this page
            Supply supply = (Supply) model.asMap().get("submittedSupply");
            if (supply != null) modelAndView.addObject("supply", supply);

            //--Prepare branches-list for several pages
            DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
            modelAndView.addObject("branchesList", branchRepository.findAllBranchIds(connectionHolder));
            ;
            connectionHolder.removeConnection();

            return modelAndView;
        }

        public ModelAndView getReportForSupplyPage(HttpServletRequest request, Model model) throws SQLException {
            //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
            DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            //--Prepare common-components of ModelAndView if we need.
            ModelAndView modelAndView = staticUtilMethods
                .customResponsiveModelView(request, model, "report-for-supply");

            //--Close Connection.
            connectionHolder.removeConnection();

            return modelAndView;
        }

        public ResDtoRetrievingData<Supply> findSupply(
            HttpServletRequest request,
            ReqDtoRetrievingData<Supply> searchingObject
        ) throws SQLException, NoSuchFieldException {
            //--Preparing data to fetch.
            searchingObject.setObjectType(Supply.class);
            searchingObject.setSearchingTable("Vattu");
            searchingObject.setSearchingTableIdName("Vattu.MAVT");
            searchingObject.setSortingCondition("ORDER BY TENVT ASC");

            return findingActionService.findingDataAndServePaginationBarFormat(request, searchingObject);
        }

        public ResDtoRetrievingData<ResDtoSupplyForImportToBuildDialog> findSupplyForOrderDetail(
            HttpServletRequest request,
            ReqDtoRetrievingData<ResDtoSupplyForImportToBuildDialog> searchingObject
        ) throws SQLException, NoSuchFieldException {
            searchingObject.setObjectType(ResDtoSupplyForImportToBuildDialog.class);
            searchingObject.setSearchingTable("Vattu");
            searchingObject.setSearchingTableIdName("Vattu.MAVT");
            searchingObject.setSortingCondition("ORDER BY TENVT ASC");
            String[] conditionsAsArr = searchingObject.getSearchingValue().split(":");
            searchingObject.setJoiningCondition(InnerJoinObject.mergeQuery(List.of(
                InnerJoinObject.builder()
                    .left("Vattu").right("CTDDH").fields("CTDDH.MAVT, CTDDH.MasoDDH, CTDDH.SOLUONG").bridge("MAVT")
                    .build()
            )));
            searchingObject.setMoreCondition("MasoDDH='" + conditionsAsArr[1] + "'");
            searchingObject.setSearchingValue("");

            return findingActionService.findingDataAndServePaginationBarFormat(request, searchingObject);
        }

        public void addSupply(HttpServletRequest request, Supply supply) throws SQLException {
            //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
            DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            //--Check If 'MAVT' is already existing or not.
            if (supplyRepository.isExistingSupplyBySupplyId(connectionHolder, supply.getSupplyId()))
                throw new DuplicateKeyException("Can't add an existing supply!");

            //--May throw DuplicateKeyException with 'MAVT'.
            if (supplyRepository.save(connectionHolder, supply) == 0)
                throw new SQLException("There's an error with SQL Server!");

            //--Close Connection.
            connectionHolder.removeConnection();
        }

        public void updateSupply(Supply supply, HttpServletRequest request) throws SQLException {
            //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
            DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            //--Check If 'MAVT' is already existing or not.
            if (!supplyRepository.isExistingSupplyBySupplyId(connectionHolder, supply.getSupplyId()))
                throw new NoSuchElementException("Updated Supply Id not found!");

            if (supplyRepository.update(connectionHolder, supply) == 0)
                throw new SQLException("There's an error with SQL Server!");

            //--Close Connection.
            connectionHolder.removeConnection();
        }

        public void deleteSupply(String supplyId, HttpServletRequest request) throws SQLException {
            //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
            DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            //--Check If 'MAVT' is already existing or not.
            if (!supplyRepository.isExistingSupplyBySupplyId(connectionHolder, supplyId))
                throw new NoSuchElementException("Deleted Supply Id not found!");

            //--Check If 'MAVT' is already in use or not.
            if (supplyRepository.isUsingSupplyBySupplyId(connectionHolder, supplyId))
                throw new IllegalStateException("Updated Supply is using!");

            if (supplyRepository.delete(connectionHolder, supplyId) == 0)
                throw new SQLException("Something wrong happened in your DBMS");
        }

        public ModelAndView getReportForDetailSuppliesInteractInfoPage(
            HttpServletRequest request,
            Model model
        ) throws SQLException {
            //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
            DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            //--Prepare common-components of ModelAndView if we need.
            ModelAndView modelAndView = staticUtilMethods
                .customResponsiveModelView(request, model, "report-for-detail-supplies-interact-info");

            //--Data for AddingEmployeeForm component.
            modelAndView.addObject("branchesList", branchRepository.findAllBranchIds(connectionHolder));

            //--Close Connection.
            connectionHolder.removeConnection();

            return modelAndView;
        }

        public ResDtoRetrievingData<ResDtoTicketsForDetailSuppliesReport> findTicketsForDetailSuppliesReport(
            HttpServletRequest request,
            ReqDtoTicketsForDetailSuppliesReport requiredInfoToSearchDetailSupplies
        ) throws SQLException {
            //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
            DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

            ResDtoRetrievingData<ResDtoTicketsForDetailSuppliesReport> result = new ResDtoRetrievingData<>();
            result.setResultDataSet(
                supplyRepository.findTicketsForDetailSuppliesReport(connectionHolder, requiredInfoToSearchDetailSupplies)
            );

            //--Close Connection.
            connectionHolder.removeConnection();

            return result;
        }
    }

    @Service
    public static class CompanyServices {

    }

    @Service
    public static class UserServices {

    }
}