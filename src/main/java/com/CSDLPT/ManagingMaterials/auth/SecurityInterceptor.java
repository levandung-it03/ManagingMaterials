package com.CSDLPT.ManagingMaterials.auth;

import com.CSDLPT.ManagingMaterials.connection.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.dto.ResDtoEmployeeInfo;
import com.CSDLPT.ManagingMaterials.model.Account;
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
        logger.info("URL: " + request.getRequestURI() + ", method: " + request.getMethod());

        try {
            ResDtoEmployeeInfo employeeInfo = (ResDtoEmployeeInfo) request
                .getSession()
                .getAttribute("employeeInfo");

            //--Use DBConnectionHolder to serve Spring Services.
            DBConnectionHolder connectionHolder = new DBConnectionHolder();

            //--May throw SQLException.
            connectionHolder.buildConnection(
                Account.builder()
                    .username(employeeInfo.getUsername())
                    .password(employeeInfo.getPassword())
                    .branch(employeeInfo.getBranch())
                    .build()
            );

            //--Redirect connectionHolder to matched ActionMethod.
            request.setAttribute("connectionHolder", connectionHolder);

            return true;
        } catch (SQLException e) {
            //--Clear everything to stop Application if there's any error.
            request.getSession().invalidate();
            request.setAttribute("connectionHolder", null);

            logger.info("PreHandlerInterceptorException: " + e);
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
        if (ex != null) logger.info("CaughtException: " + ex);
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
