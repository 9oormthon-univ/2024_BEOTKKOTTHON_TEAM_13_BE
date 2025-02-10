package com.team13.servicerecipe.feign;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor(HttpServletRequest httpServletRequest) {
        return requestTemplate -> {
            String jwtToken = extractTokenFromCookies(httpServletRequest);
            if (jwtToken != null) {
                requestTemplate.header("Authorization", "Bearer " + jwtToken);
            }
        };
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("LTK".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
