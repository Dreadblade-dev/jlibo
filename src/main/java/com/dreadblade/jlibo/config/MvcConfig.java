package com.dreadblade.jlibo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Value("${jlibo.upload.path}")
    private String uploadPath;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/book/**")
                .addResourceLocations("file://" + uploadPath + "/books/images/");
        registry.addResourceHandler("/images/author/**")
                .addResourceLocations("file://" + uploadPath + "/author/images/");
        registry.addResourceHandler("/images/user/**")
                .addResourceLocations("file://" + uploadPath + "/user/images/");
    }

    @Bean
    public MessageSource messageSource() {
        Locale.setDefault(Locale.ENGLISH);
        var messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("information-messages");

        return messageSource;
    }
}