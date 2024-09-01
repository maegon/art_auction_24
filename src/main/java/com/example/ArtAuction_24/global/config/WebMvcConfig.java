package com.example.ArtAuction_24.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://www.a-auc.art", "https://a-auc.art") // 클라이언트의 URL (배포 시에는 도메인 설정 필요)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

