package com.bbva.chatbot.helix.dto;

public sealed interface IntentRequest permits
    IntentRequest.GetTicket,
    IntentRequest.GetMyTickets,
    IntentRequest.CreateTicket,
    IntentRequest.TicketCreated,
    IntentRequest.Help,
    IntentRequest.Greeting,
    IntentRequest.Unknown {

    record GetTicket(String ticketId) implements IntentRequest {}
    record GetMyTickets() implements IntentRequest {}
    record CreateTicket() implements IntentRequest {}
    record TicketCreated(String ticketId) implements IntentRequest {}
    record Help() implements IntentRequest {}
    record Greeting() implements IntentRequest {}
    record Unknown() implements IntentRequest {}
}