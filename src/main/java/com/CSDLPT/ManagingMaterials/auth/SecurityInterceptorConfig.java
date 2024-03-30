package com.CSDLPT.ManagingMaterials.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityInterceptorConfig implements WebMvcConfigurer {
    private final SecurityInterceptor interceptorConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
            .addInterceptor(interceptorConfig)
            .addPathPatterns(List.of(
                //--GET
                "/management/**",

                //--POST
                "/service/v1/company/**",
                "/service/v1/branch/**",
                "/service/v1/user/**"
            ));
    }
}
