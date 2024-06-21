package com.CSDLPT.ManagingMaterials.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    @Bean
    public Validator hibernateValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger(Logger.class);
    }

    @Bean
    public Map<String, String> responseMessages() {
        Map<String, String> messagePairs = new HashMap<>();
        //--Successfully messages.
        messagePairs.put("succeed_add_01", "Thêm mới thông tin thành công!");
        messagePairs.put("succeed_delete_01", "Xoá thành công!");
        messagePairs.put("succeed_update_01", "Sửa đổi thành công!");

        //--Error messages.
        messagePairs.put("error_systemApplication_01", "Something wrong with application!");

        messagePairs.put("error_entity_01", "Không tìm thấy ID của đối tượng, vui lòng không sửa đổi phần mềm!");
        messagePairs.put("error_entity_02", "Trường dữ liệu không thể xoá để bảo toàn dữ liệu!");
        messagePairs.put("error_entity_03", "Dữ liệu không hợp lệ, hãy kiểm tra lại!");
        messagePairs.put("error_entity_04", "Mã đối tượng bị trùng, vui lòng thay đổi!");

        messagePairs.put("error_account_01", "Thông tin đăng nhập không đúng!");
        messagePairs.put("error_account_02", "Thông tin tài khoản đã tồn tại!");

        messagePairs.put("error_employee_01", "Mã nhân viên, hoặc CMND đã tồn tại!");

        messagePairs.put("error_supply_01", "Mã vật tư hoặc tên vật tư đã tồn tại!");
        messagePairs.put("error_supply_02", "Vật tư đang được sử dụng!");
        messagePairs.put("error_supply_03", "Vật tư có số lượng tồn quá thấp, hoặc vượt quá số lượng từ đơn đặt hàng!");
        messagePairs.put("error_supply_04", "Không tìm thấy Vật Tư");

        messagePairs.put("error_warehouse_01", "Mã kho hoặc tên kho đã tồn tại!");
        messagePairs.put("error_warehouse_02", "Không tìm thấy Mã Kho");

        messagePairs.put("error_order_01", "Không tìm thấy Mã Đơn Đặt Hàng");
        messagePairs.put("error_order_02", "Mã Đơn Đặt Hàng được cung cấp đã được lập Phiếu Nhập");
        messagePairs.put("error_order_03", "Mã Đơn Đặt Hàng đã tồn tại!");
        messagePairs.put("error_order_04", "Không thể thao tác do Đơn Đặt Hàng đã có các Chi Tiết Đơn Đặt Hàng");

        messagePairs.put("error_orderDetail_01", "Không thể cập nhật do số lượng đặt bé hơn số lượng từ Chi Tiết Phiếu Nhập");
        messagePairs.put("error_orderDetail_02", "Không thể xóa do có Chi Tiết Phiếu Nhập liên quan");

        messagePairs.put("error_suppliesImportation_01", "Mã Phiếu Nhập đã tồn tại!");
        messagePairs.put("error_suppliesImportation_02", "Không tìm thấy Mã Phiếu Nhập");
        messagePairs.put("error_suppliesImportation_03", "Không thể xoá Phiếu Nhập do đã tồn tại Phiếu Xuất tương ứng");
        messagePairs.put("error_suppliesImportation_04", "Không thể thao tác do Phiếu Nhập đã có các Chi tiết Phiếu Nhập");

        messagePairs.put("error_suppliesExportation_01", "Mã Phiếu Xuất đã tồn tại!");
        messagePairs.put("error_suppliesExportation_02", "Không tìm thấy Mã Phiếu Xuất");
        messagePairs.put("error_suppliesExportation_03", "Không thể thao tác do Phiếu Xuất đã có các Chi tiết Phiếu Xuất");

        messagePairs.put("ngu", "ngu vai cac!");

        return messagePairs;
    }

}
