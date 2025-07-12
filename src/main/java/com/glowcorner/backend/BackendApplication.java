package com.glowcorner.backend;

import com.glowcorner.backend.config.DotenvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BackendApplication.class);
		app.addInitializers(new DotenvConfig());
		app.run(args);
	}

}
