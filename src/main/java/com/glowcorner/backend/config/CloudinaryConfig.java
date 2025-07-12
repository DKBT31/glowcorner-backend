package com.glowcorner.backend.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dn6lftgbi",
                "api_key", "829596156929674",
                "api_secret", "sK1iGRKNd2JftiOaGOKBCvZQ-ho",
                "secure", true
        ));
    }
}