package com.CSDLPT.ManagingMaterials.config;

import com.CSDLPT.ManagingMaterials.dto.ResDtoUserInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class StaticUtilMethods {
    private final Map<String, String> responseMessages;

    /**Spring MVC: Customize returned ModelAndView to show Header-info and ErrMessage or SucceedMessages.**/
    public ModelAndView customResponseModelView(
        @NonNull HttpServletRequest request,
        @NonNull Map<String, Object> model,
        String viewName
    ) {
        ModelAndView modelAndView = new ModelAndView(viewName);

        Object errCode = model.get("errorCode");
        if (errCode != null)
            modelAndView.addObject("errorMessage", responseMessages.get(errCode.toString()));

        Object succeedCode = model.get("succeedCode");
        if (succeedCode != null)
            modelAndView.addObject("succeedMessage", responseMessages.get(succeedCode.toString()));

        //--Information for Header.
        ResDtoUserInfo userInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");
        if (userInfo != null)
            modelAndView.addObject("userInfo",  userInfo);

        return modelAndView;
    }

    /**Spring JdbcTemplate: this static dictionary help us find the column name of any entity**/
    public String columnNameStaticDictionary(String fieldName) throws NoSuchFieldException{
        return switch (fieldName) {
            //--Employee(NhanVien) Entity
            case "employeeId" -> "MANV";
            case "identifier" -> "CMND";
            case "lastName" -> "HO";
            case "firstName" -> "TEN";
            case "address" -> "DIACHI";
            case "birthday" -> "NGAYSINH";
            case "salary" -> "LUONG";
            case "branch" -> "MACN";
            case "deletedStatus" -> "TrangThaiXoa";
            //--More....

            default -> throw new NoSuchFieldException("Field not found");
        };
    }
}
