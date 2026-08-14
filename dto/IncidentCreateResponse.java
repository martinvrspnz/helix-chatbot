package com.bbva.chatbot.helix.dto;

import lombok.Data;
import java.util.Map;

@Data
public class IncidentCreateResponse {

    private Map<String, String> values;
}