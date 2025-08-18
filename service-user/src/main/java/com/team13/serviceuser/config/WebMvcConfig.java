package com.team13.serviceuser.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String fixedPath = uploadDir.replace("\\", "/");
        if (!fixedPath.endsWith("/")) {
            fixedPath += "/";
        }

        registry.addResourceHandler("/images/profile/**")
                .addResourceLocations("file:///" + fixedPath);

    }
}
