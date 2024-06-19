package com.CSDLPT.ManagingMaterials.config;

import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ResDtoUserInfo;

import com.CSDLPT.ManagingMaterials.EN_Branch.Branch;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
@RequiredArgsConstructor
public class StaticUtilMethods {
    private final Map<String, String> responseMessages;
    public static final String NUM_TYPE = "NUMBER";
    public static final String STR_TYPE = "TEXT";
    public static final String DATE_TYPE = "DATE";

    /** Spring MVC: Customize returned ModelAndView to show ErrMessage or SucceedMessages. **/
    public ModelAndView customResponsiveModelView(
        @NonNull HttpServletRequest request,
        @NonNull Model model,
        String pageModel
    ) {
        ModelAndView modelAndView = new ModelAndView(pageModel);
        modelAndView.addObject("errorMessage", this.getMessageFromCode("errorCode", request, model));
        modelAndView.addObject("succeedMessage", this.getMessageFromCode("succeedCode", request, model));

        //--Prepare data for Header of Pages.
        ResDtoUserInfo userInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");
        if (userInfo != null)
            modelAndView.addObject("userInfo",  userInfo);

        return modelAndView;
    }
    private String getMessageFromCode(String codeType, HttpServletRequest request, Model model) {
        Map<String, Object> fieldValuePairsInModel = model.asMap();
        Object messageCode = fieldValuePairsInModel.getOrDefault(codeType, null);
        if (messageCode == null) {
            messageCode = request.getSession().getAttribute(codeType);
            request.getSession().removeAttribute(codeType);
        }
        if (messageCode != null) {
            return responseMessages.getOrDefault(messageCode.toString(), messageCode.toString());
        }
        return null;
    }

    /** Spring JdbcTemplate: this static dictionary help us find the column name of any entity **/
    public List<String> columnNameStaticDictionary(String fieldName) throws NoSuchFieldException{
        return switch (fieldName) {
            //--Employee(NhanVien) Entity
            case "Employee" -> List.of("NhanVien", "MANV", NUM_TYPE);
            case "employeeId", "MANV" -> List.of("MANV", "employeeId", NUM_TYPE);
            case "employeeIdAsFk", "NhanVien" -> List.of("MANV", "EmployeeFromFk.MANV", "EmployeeFromFk", NUM_TYPE);
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
            case "Warehouse" -> List.of("Kho", "MAKHO", STR_TYPE);
            case "warehouseId", "MAKHO" -> List.of("MAKHO", "warehouseId", STR_TYPE);
            case "warehouseIdAsFk", "Kho" -> List.of("MAKHO", "WarehouseFromFk.MAKHO", "WarehouseFromFk", STR_TYPE);
            case "warehouseName", "TENKHO" -> List.of("TENKHO", "warehouseName", STR_TYPE);

            //--Supply(Vattu) Entity
            case "Supply", "ResDtoSupplyForImportToBuildDialog" -> List.of("Vattu", "MAVT", STR_TYPE);
            case "supplyIdAsFk", "Vattu" -> List.of("MAVT", "SupplyFromFk.MAVT", "SupplyFromFk", STR_TYPE);
            case "supplyId", "MAVT" -> List.of("MAVT", "supplyId", STR_TYPE);
            case "supplyName", "TENVT" -> List.of("TENVT", "supplyName", STR_TYPE);
            case "unit", "DVT" -> List.of("DVT", "unit", STR_TYPE);
            case "quantityInStock", "SOLUONGTON" -> List.of("SOLUONGTON", "quantityInStock", NUM_TYPE);

            //--Order(DatHang) Entity
            case "Order" -> List.of("MasoDDH", "DatHang", STR_TYPE);
            case "orderId", "MasoDDH" -> List.of("MasoDDH", "orderId", STR_TYPE);
            case "supplier", "NhaCC" -> List.of("NhaCC", "supplier", STR_TYPE);
            case "createdDate", "NGAY" -> List.of("NGAY", "createdDate", DATE_TYPE);
            case "suppliesQuantity", "SOLUONG" -> List.of("SOLUONG", "suppliesQuantity", NUM_TYPE);
            case "price", "DONGIA" -> List.of("DONGIA", "price", NUM_TYPE);
            case "employeeFullName", "HOTEN" -> List.of("HOTEN", "employeeFullName", STR_TYPE);

            //--OrderDetail(CTDDH) Entity
            case "orderDetailId", "CTDDH" -> List.of("CTDDH", "OrderDetailFromFk.CTDDH", "OrderDetailFromFk", STR_TYPE);
            case "suppliesQuantityFromOrderDetailAsFk" ->
                List.of("SOLUONG", "OrderDetailFromFk.SOLUONG", "OrderDetailFromFk", NUM_TYPE);

            //--SuppliesImportation(PhieuNhap) Entity
            case "SuppliesImportation" -> List.of("PhieuNhap", "MAPN", STR_TYPE);
            case "suppliesImportationId", "MAPN" -> List.of("MAPN", "suppliesImportationId", STR_TYPE);
            case "suppliesImportationIdAsFk", "PhieuNhap" ->
                List.of("MAPN", "SuppliesImportationFromFk.MAPN", "SuppliesImportationFromFk", STR_TYPE);

            //--SuppliesExportation(PhieuXuat) Entity
            case "SuppliesExportation" -> List.of("PhieuXuat", "MAPX", STR_TYPE);
            case "suppliesExportationId", "MAPX" -> List.of("MAPX", "suppliesExportationId", STR_TYPE);
            case "suppliesExportationIdAsFk", "PhieuXuat" ->
                List.of("MAPX", "SuppliesExportationFromFk.MAPX", "SuppliesExportationFromFk", STR_TYPE);
            case "customerFullName", "HOTENKH" -> List.of("HOTENKH", "customerFullName", STR_TYPE);
            //--More....

            default -> throw new NoSuchFieldException("Field not found");
        };
    }

    /** Spring JdbcTemplate: this static method help us find converts java.util.Date to java.sql.Date and fix Timezone **/
    public java.sql.Date dateUtilToSqlDate(java.util.Date inputDate) {
        int[] dateAsArr = Arrays.stream(
                new SimpleDateFormat("dd/MM/yyyy")
                    .format(inputDate)
                    .split("/")
            )
            .mapToInt(Integer::parseInt)
            .toArray();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        //--Tháng (0-indexed)
        calendar.set(dateAsArr[2], dateAsArr[1] - 1, dateAsArr[0]);

        return new java.sql.Date(calendar.getTimeInMillis());
    }

    /** Spring Core: this static method help us convert time millisecond to LocalDateTime **/
    public LocalDateTime milisToLocalDateTime(Long mls) {
        Instant instant = Instant.ofEpochMilli(mls);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
