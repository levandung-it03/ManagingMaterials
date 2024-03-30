package com.CSDLPT.ManagingMaterials.config;

//import com.CSDLPT.ManagingMaterials.auth.SecurityInterceptor;
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
        messagePairs.put("error_entity_02", "Trường dữ liệu không thể xoá, thay vào đó hãy đổi trạng thái để bảo toàn dữ liệu!");
        messagePairs.put("error_entity_03", "Dữ liệu không hợp lệ, hãy kiểm tra lại!");

        messagePairs.put("error_account_01", "Username không đúng!");
        messagePairs.put("error_account_02", "Username đã tồn tại!");
        messagePairs.put("error_account_03", "Mật khẩu không đúng!");

        return messagePairs;
    }
}
