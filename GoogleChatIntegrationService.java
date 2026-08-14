package com.bbva.chatbot.helix.service.impl;

import com.bbva.chatbot.helix.dto.GoogleChatEvent;
import com.bbva.chatbot.helix.dto.IncidentCreateResponseDto;
import com.bbva.chatbot.helix.service.HelixService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleChatIntegrationService {

    private final ChatBotService chatBotService;
    private final HelixService helixService;
    private final GoogleChatResponseService googleChatResponseService;

    public Map<String, Object> handleEvent(Map<String, Object> body) {
        GoogleChatEvent event = GoogleChatEvent.parse(body);
        log.info("type: {}", event.type());

        String text = event.text();

        if ("CARD_CLICKED".equals(event.type())) {
            if ("onClickOkButton".equals(event.actionMethodName())) {
                String registry = event.getFormInputValue("registry");
                String title = event.getFormInputValue("title");
                String description = event.getFormInputValue("description");
                IncidentCreateResponseDto incidentCreateResponse = helixService.createIncident(registry, title,
                        description);
                String ticketId = incidentCreateResponse.getIncidentNumber();
                text = "ticket_created:" + ticketId;
            } else {
                text = "help";
            }
        } else if (!"ADDED_TO_SPACE".equals(event.type()) && !"MESSAGE".equals(event.type())) {
            text = "error";
        }

        String response = chatBotService.process(text, event.email(), event.displayName());

        if ("SHOW_CREATE_FORM".equals(response)) {
            return googleChatResponseService.createIncidentFormCard();
        }

        return googleChatResponseService.createTextResponse(response);
    }
}
