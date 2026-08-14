package com.bbva.chatbot.helix.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import com.bbva.chatbot.helix.service.impl.GoogleChatIntegrationService;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/google")
@Slf4j
public class ChatGoogleController {

    private final GoogleChatIntegrationService googleChatIntegrationService;

    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, Object> body) {
        return googleChatIntegrationService.handleEvent(body);
    }
}