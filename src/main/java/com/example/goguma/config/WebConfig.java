package com.example.goguma.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://d9gox7s2vayxc.cloudfront.net", "http://gogumacat-frontend.s3-website.ap-northeast-2.amazonaws.com", "http://127.0.0.1:5500")
                .allowedMethods("*");
    }
}