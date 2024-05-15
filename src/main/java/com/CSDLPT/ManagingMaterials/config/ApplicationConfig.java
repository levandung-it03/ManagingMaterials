package com.CSDLPT.ManagingMaterials.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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

        messagePairs.put("error_warehouse_01", "Mã kho hoặc tên kho đã tồn tại!");

        messagePairs.put("ngu", "ngu vai cac!");

        return messagePairs;
    }

}
