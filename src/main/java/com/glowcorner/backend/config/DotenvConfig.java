package com.glowcorner.backend.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class to load environment variables from .env file
 * This allows team members to simply copy the .env file and have everything
 * work
 */
@Component
public class DotenvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            // Load .env file from the root of the project
            Dotenv dotenv = Dotenv.configure()
                    .directory("./") // Look in the current directory (project root)
                    .ignoreIfMissing() // Don't fail if .env file is missing
                    .load();

            // Convert to Map for Spring PropertySource
            Map<String, Object> envMap = new HashMap<>();
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                envMap.put(key, value);
                System.out.println("✅ Loaded environment variable: " + key);
            });

            // Add the properties to Spring's environment
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            environment.getPropertySources().addFirst(new MapPropertySource("dotenv", envMap));

            System.out.println("✅ .env file loaded successfully with " + envMap.size() + " variables");

        } catch (Exception e) {
            System.err.println("⚠️  Warning: Could not load .env file: " + e.getMessage());
            System.err.println("Make sure the .env file exists in the project root directory");
        }
    }
}
