package com.bbva.chatbot.helix.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "helix-login-client", url = "${helix.api.url}")
public interface HelixLoginClient {

    @PostMapping(value = "/api/jwt/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    );
}
