package com.CSDLPT.ManagingMaterials.config;

import com.CSDLPT.ManagingMaterials.dto.ResDtoUserInfo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@RequiredArgsConstructor
public class StaticUtilMethods {
    private final Map<String, String> responseMessages;
    public static final String NUM_TYPE = "NUMBER";
    public static final String STR_TYPE = "TEXT";
    public static final String DATE_TYPE = "DATE";

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
    public List<String> columnNameStaticDictionary(String fieldName) throws NoSuchFieldException{
        return switch (fieldName) {
            //--Employee(NhanVien) Entity
            case "employeeId", "MANV" ->List.of("MANV", "employeeId", NUM_TYPE);
            case "identifier", "CMND" -> List.of("CMND", "identifier", STR_TYPE);
            case "lastName", "HO" -> List.of("HO", "lastName", STR_TYPE);
            case "firstName", "TEN" -> List.of("TEN", "firstName", STR_TYPE);
            case "fullName" -> List.of("CONCAT(HO, ' ', TEN)", "fullName", STR_TYPE);
            case "address", "DIACHI" -> List.of("DIACHI", "address", STR_TYPE);
            case "birthday", "NGAYSINH" -> List.of("NGAYSINH", "birthday", DATE_TYPE);
            case "salary", "LUONG" -> List.of("LUONG", "salary", NUM_TYPE);
            case "branch", "MACN" -> List.of("MACN", "branch", STR_TYPE);
            case "deletedStatus", "TrangThaiXoa" -> List.of("TrangThaiXoa", "deletedStatus", NUM_TYPE);

            //--Warehouse(Kho) Entity
            case "warehouseId", "MAKHO" -> List.of("MAKHO", "warehouseId", STR_TYPE);
            case "warehouseName", "TENKHO" -> List.of("TENKHO", "warehouseName", STR_TYPE);

            //--Supply(VatTu) Entity
            case "supplyId", "MAVT" -> List.of("MAVT", "supplyId", STR_TYPE);
            case "supplyName", "TENVT" -> List.of("TENVT", "supplyName", STR_TYPE);
            case "unit", "DVT" -> List.of("DVT", "unit", STR_TYPE);
            case "quantityInStock", "SOLUONGTON" -> List.of("SOLUONGTON", "quantityInStock", NUM_TYPE);
            //--Order(DatHang) Entity
            case "orderId", "MasoDDH" -> List.of("MasoDDH", "orderId", STR_TYPE);
            case "supplier", "NhaCC" -> List.of("NhaCC", "supplier", STR_TYPE);
            case "createdDate", "NGAY" -> List.of("NGAY", "createdDate", DATE_TYPE);
            case "quantitySupply", "SOLUONG" -> List.of("SOLUONG", "quantitySupply", NUM_TYPE);
            case "price", "DONGIA" -> List.of("DONGIA", "price", NUM_TYPE);
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
