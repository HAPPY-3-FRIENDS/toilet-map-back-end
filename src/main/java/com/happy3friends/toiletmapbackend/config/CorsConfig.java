package com.happy3friends.toiletmapbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.OPTIONS.name(), HttpMethod.DELETE.name())
                        .allowedHeaders("*")
                        .allowedOrigins("http://localhost:8000",
                                "http://localhost:8081",
                                "http://localhost:3000",
                                "https://toiletmap.azurewebsites.net",
                                "https://toilet-map.azurewebsites.net");
            }
        };
    }
}
