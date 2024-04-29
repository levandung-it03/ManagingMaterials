package com.CSDLPT.ManagingMaterials.service.BranchService;

import com.CSDLPT.ManagingMaterials.config.StaticUtilMethods;
import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.dto.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.model.Supply;
import com.CSDLPT.ManagingMaterials.model.PageObject;
import com.CSDLPT.ManagingMaterials.repository.BranchRepository;
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

//    public void addSupply(HttpServletRequest request, Supply supply) throws DuplicateKeyException, SQLException {
//        //--Get the Connection from 'request' as Redirected_Attribute from Interceptor.
//        DBConnectionHolder connectionHolder = (DBConnectionHolder) request.getAttribute("connectionHolder");
//
//        //--Get the auto-generated supplyId from Session (from getManageSupplyPage()).
//        String autoSupplyIdFromSession = request.getSession().getAttribute("addingSupplyId").toString();
//        supply.setSupplyId(Integer.parseInt(autoSupplyIdFromSession));
//
//        //--Get Current_Login_User from Session.
//        ResDtoUserInfo userInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");
//
//        //--Check If 'MANV' is already existing or not.
//        if (supplyRepository.isExistingSupplyByIdentifier(connectionHolder, supply.getIdentifier()))
//            throw new DuplicateKeyException("Can't add an existing supply!");
//
//        //--Supply must have the same 'branch' with Current_Login_User.
//        supply.setBranch(userInfo.getBranch());
//
//        //--May throw DuplicateKeyException with ('CNMD', 'TrangThaiXoa') pair of fields.
//        if (supplyRepository.save(connectionHolder, supply) == 0)
//            throw new DuplicateKeyException("There's an error with SQL Server!");
//
//        //--Close Connection.
//        connectionHolder.removeConnection();
//    }
}
