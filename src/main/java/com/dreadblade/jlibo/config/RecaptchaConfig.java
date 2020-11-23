package com.dreadblade.jlibo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
public class RecaptchaConfig {
    @Value("${google.recaptcha.key.site}")
    private String siteKey;

    @Value("${google.recaptcha.key.secret}")
    private String secretKey;

    @Bean
    public RecaptchaConfig getRecaptchaConfig() {
        return new RecaptchaConfig();
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}