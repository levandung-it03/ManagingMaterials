package com.CSDLPT.ManagingMaterials.auth.authConfig;

import com.CSDLPT.ManagingMaterials.EN_Account.RoleEnum;
import com.CSDLPT.ManagingMaterials.database.DBConnectionHolder;
import com.CSDLPT.ManagingMaterials.EN_Account.dtos.ResDtoUserInfo;
import com.CSDLPT.ManagingMaterials.EN_Account.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final DBConnectionHolder connectionHolder;

    @Override
    public boolean preHandle(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull Object handler
    ) throws Exception {
        logger.info("URL: " + request.getRequestURI() + ", method: " + request.getMethod());

        try {
            ResDtoUserInfo userInfo = (ResDtoUserInfo) request.getSession().getAttribute("userInfo");

            if (!request.getRequestURI().contains(switch (userInfo.getBranch()) {
                case "CHINHANH" -> "branch";
                case "CONGTY" -> "company";
                case "USER" -> "user";
                default -> "null";
            })) {
                response.sendRedirect("/home");
                return false;
            }

            //--May throw SQLException.
            connectionHolder.buildConnection(
                Account.builder()
                    .username(userInfo.getUsername())
                    .password(userInfo.getPassword())
                    .branch(userInfo.getBranch())
                    .build()
            );

            //--Redirect connectionHolder to matched ActionMethod.
            request.setAttribute("connectionHolder", connectionHolder);

            return true;
        } catch (NullPointerException | SQLException e) {
            //--Clear everything to stop Application if there's any error.
            request.setAttribute("connectionHolder", null);
            request.getSession().removeAttribute("userInfo");
            request.getSession().removeAttribute("errorMessage");
            request.getSession().removeAttribute("succeedMessage");

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
