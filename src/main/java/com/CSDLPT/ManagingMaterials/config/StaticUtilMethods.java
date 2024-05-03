package com.CSDLPT.ManagingMaterials.config;

import com.CSDLPT.ManagingMaterials.dto.ResDtoUserInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.time.LocalDate;
import java.util.TimeZone;

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
            case "employeeId", "MANV" -> "MANV";
            case "identifier", "CMND" -> "CMND";
            case "lastName", "HO" -> "HO";
            case "firstName", "TEN" -> "TEN";
            case "fullName" -> "CONCAT(HO, ' ', TEN)";
            case "address", "DIACHI" -> "DIACHI";
            case "birthday", "NGAYSINH" -> "NGAYSINH";
            case "salary", "LUONG" -> "LUONG";
            case "branch", "MACN" -> "MACN";
            case "deletedStatus", "TrangThaiXoa" -> "TrangThaiXoa";
            case "warehouseId", "MAKHO" -> "MAKHO";
            case "warehouseName", "TENKHO" -> "TENKHO";
            case "supplyId", "MAVT" -> "MAVT";
            case "supplyName", "TENVT" -> "TENVT";
            case "unit", "DVT" -> "DVT";
            case "quantityInStock", "SOLUONGTON" -> "SOLUONGTON";
            //--More....

            default -> throw new NoSuchFieldException("Field not found");
        };
    }

    /**Spring JdbcTemplate: this static method help us find converts java.util.Date to java.sql.Date and fix Timezone**/
    public java.sql.Date dateUtilToSqlDate(java.util.Date inputDate) {
        int[] arrDate = Arrays.stream(new SimpleDateFormat("dd/MM/yyyy").format(inputDate).split("/"))
            .mapToInt(Integer::parseInt)
            .toArray();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT-17"));
        //--Tháng (0-indexed)
        calendar.set(arrDate[2], arrDate[1] - 1, arrDate[0]);

        return new java.sql.Date(calendar.getTimeInMillis());
    }
}
