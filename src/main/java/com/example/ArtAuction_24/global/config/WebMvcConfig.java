package com.example.ArtAuction_24.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${custom.genFileDirPath}")
    private String fileDirPath;

    @Override
    public void addResourceHandlers (ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/file/**")
                .addResourceLocations("file:///" + fileDirPath + "/");


    }
}

