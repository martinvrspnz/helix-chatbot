package com.bbva.chatbot.helix.dto;

import lombok.Data;
import java.util.Map;

@Data
public class IncidentCreateRequest {
    private Map<String, Object> values;
}