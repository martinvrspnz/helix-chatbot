package com.bbva.chatbot.helix.service.impl;

import com.bbva.chatbot.helix.client.HelixLoginClient;
import com.bbva.chatbot.helix.service.HelixAuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HelixAuthServiceImpl implements HelixAuthService {

    private final HelixLoginClient loginClient;

    @Value("${helix.login.username}")
    private String username;

    @Value("${helix.login.password}")
    private String password;

    @Override
    @Cacheable("helixToken")
    public String getAuthToken() {
        log.info("No hay token en caché o expiró. Solicitando nuevo token a Helix...");
        return loginClient.login(username, password);
    }
}
