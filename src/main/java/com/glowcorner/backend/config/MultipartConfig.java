package com.glowcorner.backend.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import jakarta.servlet.MultipartConfigElement; // Import đúng thư viện

@Configuration
public class MultipartConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(10)); // Giới hạn file 10MB
        factory.setMaxRequestSize(DataSize.ofMegabytes(20)); // Tổng request tối đa 20MB
        return factory.createMultipartConfig();
    }
}
