package com.CSDLPT.ManagingMaterials.auth;

import com.CSDLPT.ManagingMaterials.connection.DBConnection;
import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.dto.ReqDtoAccount;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityInterceptor implements HandlerInterceptor {
    private final Logger logger;

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws Exception {
        logger.info("[===LOGGER===] URL: " + request.getRequestURI() + ", method: " + request.getMethod());

        try {
            HttpSession session = request.getSession();
            ReqDtoAccount account = ReqDtoAccount.builder()
                    .username((String) session.getAttribute("username"))
                    .password((String) session.getAttribute("key"))
                    .branch((String) session.getAttribute("branch"))
                    .build();
            DBConnectionHolder.setConnection(DBConnection.getInstance().getConnection(account));
            return true;
        } catch (SQLException e) {
            logger.info("[===LOGGER===] PreHandlerInterceptorException: " + e.getMessage());
            response.sendRedirect("/login");
            return false;
        }
    }

    @Override
    public void postHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler, ModelAndView modelAndView
    ) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            Exception ex
    ) throws Exception {
        if (ex != null) logger.info("[===LOGGER===] CaughtException: " + ex.getMessage());
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
