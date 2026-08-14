package com.bbva.chatbot.helix.dto;

public record CreateIncidentRequest(String registry, String title, String description) {
}