package com.bbva.chatbot.helix.controller;

import com.bbva.chatbot.helix.dto.IntranetChatRequest;
import com.bbva.chatbot.helix.dto.IntranetChatResponse;
import com.bbva.chatbot.helix.service.ChatAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/intranet-chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class IntranetChatController {

    private final ChatAiService chatAiService;

    @PostMapping("/iniciar")
    public ResponseEntity<IntranetChatResponse> iniciarConversacion(@RequestParam String usuario) {
        String responseText = chatAiService.iniciarConversacion(usuario);
        return ResponseEntity.ok(new IntranetChatResponse(responseText));
    }

    @PostMapping("/iniciar/stream")
    public ResponseEntity<IntranetChatResponse> iniciarConversacionStream(@RequestParam String usuario) {
        String responseText = chatAiService.iniciarConversacion(usuario);
        return ResponseEntity.ok(new IntranetChatResponse(responseText));
    }

    @PostMapping("/enviar")
    public ResponseEntity<IntranetChatResponse> enviarMensaje(@RequestBody IntranetChatRequest request) {
        String responseText = chatAiService.chat(request.getMensaje(), request.getUsuarioId());
        return ResponseEntity.ok(new IntranetChatResponse(responseText));
    }

    @PostMapping(value = "/enviar/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> enviarMensajeStream(@RequestBody IntranetChatRequest request) {
        return chatAiService.chatStream(request.getMensaje(), request.getUsuarioId());
    }
}
