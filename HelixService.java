package com.bbva.chatbot.helix.service;

import com.bbva.chatbot.helix.dto.IncidentCreateResponseDto;
import com.bbva.chatbot.helix.dto.IncidentResponseDto;

import java.util.List;

public interface HelixService {
    IncidentCreateResponseDto createIncident(String registry, String title, String description);

    IncidentResponseDto getIncidentDetails(String ticketId);

    List<IncidentResponseDto> getMyTicketsByEmail(String email);

    List<IncidentResponseDto> getActiveTicketsByRegistry(String registry);

    List<IncidentResponseDto> getAllTicketsByRegistry(String registry);
}