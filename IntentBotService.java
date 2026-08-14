package com.bbva.chatbot.helix.service.impl;

import com.bbva.chatbot.helix.dto.IntentRequest;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IntentBotService {

    private static final Pattern GET_TICKET_PATTERN = Pattern
            .compile("(?i).*(?:estado|consultar|ver).*ticket\\s+(INC[0-9]+).*");

    private static final Pattern CREATE_TICKET_PATTERN = Pattern.compile(
            "(?i).*(crear|reportar|registrar|abrir|generar|nuevo)\\s+(un |una )?(ticket|incidencia|problema|caso).*");

    private static final Pattern MY_TICKETS_PATTERN = Pattern.compile("(?i).*mis tickets.*");

    private static final Pattern TICKET_CREATED_PATTERN = Pattern.compile("(?i)ticket_created:(INC[0-9]+)");

    public IntentRequest parse(String msg) {

        String text = msg.toLowerCase().trim();

        Matcher getTicketMatcher = GET_TICKET_PATTERN.matcher(text);
        if (getTicketMatcher.matches()) {
            String ticketId = getTicketMatcher.group(1).toUpperCase();

            if (!ticketId.isEmpty()) {
                return new IntentRequest.GetTicket(ticketId);
            }
        }

        if (MY_TICKETS_PATTERN.matcher(text).matches()) {
            return new IntentRequest.GetMyTickets();
        }

        if (CREATE_TICKET_PATTERN.matcher(text).matches()) {
            return new IntentRequest.CreateTicket();
        }

        Matcher ticketCreatedMatcher = TICKET_CREATED_PATTERN.matcher(text);
        if (ticketCreatedMatcher.matches()) {
            String ticketId = ticketCreatedMatcher.group(1).toUpperCase();
            return new IntentRequest.TicketCreated(ticketId);
        }

        if (isHelp(text)) {
            return new IntentRequest.Help();
        }

        if (isGreeting(text)) {
            return new IntentRequest.Greeting();
        }

        return new IntentRequest.Unknown();
    }

    private boolean isGreeting(String text) {
        return text.matches(".*\\b(hola|buenas|hello|hi|buenos dias|buenas tardes)\\b.*");
    }

    private boolean isHelp(String text) {
        return text.matches(".*\\b(ayuda|help|opciones|menu)\\b.*");
    }

}
