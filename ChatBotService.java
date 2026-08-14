package com.bbva.chatbot.helix.service.impl;

import com.bbva.chatbot.helix.dto.IntentRequest;
import com.bbva.chatbot.helix.dto.IncidentResponseDto;
import com.bbva.chatbot.helix.service.HelixService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatBotService {

    private final IntentBotService intentBotService;
    private final HelixService helixService;

    public String process(String text, String user, String displayName) {

        IntentRequest intent = intentBotService.parse(text);

        return switch (intent) {

            case IntentRequest.GetTicket(String ticketId) -> {

                IncidentResponseDto ticketDetails = helixService.getIncidentDetails(ticketId);

                if (ticketDetails != null) {
                    yield "El estado actual de tu ticket *" + ticketDetails.getIncidentNumber() + "* es: *"
                            + ticketDetails.getStatus() + "*\\n" +
                            "Descripción: " + ticketDetails.getDescription() + "\\n" +
                            "Asignado a: " + ticketDetails.getAssignedGroup();
                } else {
                    yield "Lo siento, no pude encontrar el ticket *" + ticketId + "* en el sistema.";
                }
            }

            case IntentRequest.GetMyTickets() -> {

                List<IncidentResponseDto> tickets = helixService.getMyTicketsByEmail(user);

                if (tickets.isEmpty()) {
                    yield "No tienes tickets activos en este momento.";
                }

                StringBuilder sb = new StringBuilder("Tus tickets activos:\\n\\n");
                for (IncidentResponseDto ticket : tickets) {
                    sb.append("🎫 *").append(ticket.getIncidentNumber()).append("*\\n")
                            .append("Estado: ").append(ticket.getStatus()).append("\\n")
                            .append("Asunto: ").append(ticket.getDescription()).append("\\n\\n");
                }
                yield sb.toString();
            }

            case IntentRequest.TicketCreated(String ticketId) -> "ticket " + ticketId + " creado correctamente";

            case IntentRequest.Greeting() ->
                "Hola " + displayName
                        + " 👋\\n\\n¿En qué puedo ayudarte?\\n\\nPuedes consultar el estado de tu ticket así:\\n👉 estado del ticket INC000000004817\\n👉 mis tickets";

            case IntentRequest.CreateTicket() -> "SHOW_CREATE_FORM";

            case IntentRequest.Help() ->
                "Puedes usar los siguientes comandos:\\n- ticket INC000000004817\\n- estado ticket INC000000004817\\n- mis tickets";

            case IntentRequest.Unknown() ->
                "No entendí tu solicitud. Puedes escribir algo como: 'estado del ticket INC000000004817' o 'help'";
        };
    }
}
