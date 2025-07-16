package com.example.labourApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // apply to all paths
                        .allowedOrigins("https://labour-app-react.vercel.app/" , "http://localhost:3000") // frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS" , "PATCH")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
