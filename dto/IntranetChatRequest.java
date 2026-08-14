package com.bbva.chatbot.helix.dto;

import lombok.Data;

@Data
public class IntranetChatRequest {
    private String usuarioId;
    private String mensaje;
}
