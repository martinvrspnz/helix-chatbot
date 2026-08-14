package com.bbva.chatbot.helix.config;

import com.bbva.chatbot.helix.service.HelixAuthService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelixAuthInterceptor implements RequestInterceptor {

    private final HelixAuthService helixAuthService;


    public HelixAuthInterceptor(HelixAuthService helixAuthService) {
        this.helixAuthService = helixAuthService;
    }

    @Override
    public void apply(RequestTemplate template) {

        if (!template.url().contains("/api/jwt/login")) {


            String token = helixAuthService.getAuthToken();
            String value = "AR-JWT " + token;

            template.header("Authorization", value);
        }
    }
}