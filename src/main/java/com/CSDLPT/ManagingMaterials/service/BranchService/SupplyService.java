package com.CSDLPT.ManagingMaterials.service.BranchService;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.dto.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.dto.ResDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.model.Employee;
import com.CSDLPT.ManagingMaterials.model.Supply;
import com.CSDLPT.ManagingMaterials.model.Supply;
import com.CSDLPT.ManagingMaterials.model.PageObject;
import com.CSDLPT.ManagingMaterials.repository.SupplyRepository;
import com.CSDLPT.ManagingMaterials.service.GeneralService.FindingActionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplyService {
    private final StaticUtilMethods staticUtilMethods;
    private final SupplyRepository supplyRepository;
    private final FindingActionService findingActionService;
    private final ResDtoRetrievingData<Supply> resDtoRetrievingData;

    public ModelAndView getManageSupplyPage(HttpServletRequest request, Model model) throws SQLException {
        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        //--Prepare common-components of ModelAndView if we need.
        ModelAndView modelAndView = staticUtilMethods.customResponseModelView(request, model.asMap(), "manage-supply");

        //--If there's an error when handle data with DB, take the submitted-supply-info and give it back to this page
        Supply supply = (Supply) model.asMap().get("submittedSupply");
        if (supply != null)   modelAndView.addObject("supply", supply);

        //--Prepare data of supply-list.
        PageObject pageObj = new PageObject(request);
        List<Supply> supplyList = supplyRepository.findAll(connectionHolder, pageObj);

        //--Data for SupplyList component.
        modelAndView.addObject("supplyList", supplyList);
        modelAndView.addObject("currentPage", pageObj.getPage());

        //--Close Connection.
        connectionHolder.removeConnection();

        return modelAndView;
    }

    public ResDtoRetrievingData<Supply> findSupply(
            HttpServletRequest request,
            ReqDtoRetrievingData<Supply> searchingObject
    ) throws SQLException {
        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        searchingObject.setObjectType(Supply.class);
        searchingObject.setSearchingTable("Vattu");
        searchingObject.setSearchingTableIdName("MAVT");
        searchingObject.setSortingCondition("ORDER BY TENVT ASC");

        //--IoC at here.
        resDtoRetrievingData.setResultDataSet(findingActionService
                .findingDataWithPaging(connectionHolder, searchingObject));
        resDtoRetrievingData.setTotalObjectsQuantityResult(findingActionService
                .countAllByCondition(connectionHolder, searchingObject));

        //--Close Connection.
        connectionHolder.removeConnection();

        return resDtoRetrievingData;
    }

    public void addSupply(HttpServletRequest request, Supply supply) throws DuplicateKeyException, SQLException {
        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");

        //--Check If 'MAVT' is already existing or not.
        if (supplyRepository.isExistingSupplyBySupplyId(connectionHolder, supply.getSupplyId()))
            throw new DuplicateKeyException("Can't add an existing supply!");

        //--May throw DuplicateKeyException with 'MAVT'.
        if (supplyRepository.save(connectionHolder, supply) == 0)
            throw new DuplicateKeyException("There's an error with SQL Server!");

        //--Close Connection.
        connectionHolder.removeConnection();
    }

//    public void updateSupply(Supply supply, HttpServletRequest request) throws SQLException {
//        final String updatedId = request.getParameter("supplyId");
//
//        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
//        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
//
//        if (!updatedId.equals(supply.getSupplyId()))
//            throw new NoSuchElementException("Supply Id is invalid");
//
//        if (supplyRepository.update(connectionHolder, supply) == 0)
//            throw new SQLException("There's an error with SQL Server!");
//
//        //--Close Connection.
//        connectionHolder.removeConnection();
//    }
}
