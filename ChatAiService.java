package com.bbva.chatbot.helix.service;

import reactor.core.publisher.Flux;

public interface ChatAiService {
    String chat(String message, String usuarioId);

    Flux<String> chatStream(String message, String usuarioId);

    String iniciarConversacion(String usuario);

    Flux<String> iniciarConversacionStream(String usuario);
}
