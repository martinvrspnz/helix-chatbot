package com.bbva.chatbot.helix.service.impl;

import com.bbva.chatbot.helix.client.HelixIncidentClient;
import com.bbva.chatbot.helix.dto.IncidentCreateRequest;
import com.bbva.chatbot.helix.dto.IncidentCreateResponse;
import com.bbva.chatbot.helix.dto.IncidentCreateResponseDto;
import com.bbva.chatbot.helix.dto.IncidentResponseDto;
import com.bbva.chatbot.helix.dto.IncidentSearchResponse;
import com.bbva.chatbot.helix.mapper.HelixMapper;
import com.bbva.chatbot.helix.service.HelixService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class HelixServiceImpl implements HelixService {

    private final HelixIncidentClient incidentClient;
    private final HelixMapper helixMapper;

    @Override
    public IncidentCreateResponseDto createIncident(String registry, String title, String description) {
        log.info("Enviando solicitud de creación a Helix...");
        Map<String, Object> incidentData = new HashMap<>();
        incidentData.put("Assigned Support Company", "BBVA Perú");
        incidentData.put("Assigned Support Organization", "ENGINEERING - INFRASTRUCTURE & OPERATIONS");
        incidentData.put("Assigned Group", "MDA Nivel 1");
        incidentData.put("Login_ID", registry);
        incidentData.put("Service_Type", 3);
        incidentData.put("Status", 1);
        incidentData.put("Impact", 1000);
        incidentData.put("Urgency", 1000);
        incidentData.put("Description", title);
        incidentData.put("Detailed_Decription", description);
        incidentData.put("Reported Source", "Other");
        IncidentCreateRequest request = new IncidentCreateRequest();
        request.setValues(incidentData);

        IncidentCreateResponse rawResponse = incidentClient.createIncident(request, "values(Incident Number)");
        return helixMapper.toCreateResponseDto(rawResponse);
    }

    @Override
    public IncidentResponseDto getIncidentDetails(String ticketId) {
        log.info("Consultando detalles del ticket {} en Helix...", ticketId);

        String query = String.format("'Incident Number'=\"%s\"", ticketId);
        String fields = "values(Incident Number,Customer Login ID,Status,Priority,Description,Detailed Decription,Submit Date,Assignee Login ID,Assigned Group,Assignee,Resolution,Categorization Tier 1,Categorization Tier 2,Categorization Tier 3,Categorization Tier 4)";

        IncidentSearchResponse response = incidentClient.searchIncidents(query, fields);

        if (response != null && response.getEntries() != null && !response.getEntries().isEmpty()) {
            return helixMapper.toIncidentResponseDto(response.getEntries().getFirst().getValues());
        }

        return null;
    }

    @Override
    public List<IncidentResponseDto> getMyTicketsByEmail(String email) {
        log.info("Buscando tickets activos para: {}", email);

        String query = String
                .format("'Internet E-mail'=\"%s\" AND 'Status' IN (\"Assigned\",\"In Progress\",\"Pending\")", email);
        String fields = "values(Incident Number,Customer Login ID,Status,Priority,Description,Detailed Decription,Submit Date,Assignee Login ID,Assigned Group,Assignee,Resolution,Categorization Tier 1,Categorization Tier 2,Categorization Tier 3,Categorization Tier 4)";

        IncidentSearchResponse response = incidentClient.searchIncidents(query, fields);

        if (response != null && response.getEntries() != null) {
            return helixMapper.toIncidentResponseDtoList(response.getEntries());
        }

        return Collections.emptyList();
    }

    @Override
    public List<IncidentResponseDto> getActiveTicketsByRegistry(String registry) {
        log.info("Buscando tickets activos para: {}", registry);

        String query = String
                .format("'Customer Login ID'=\"%s\" AND 'Status' IN (\"Assigned\",\"In Progress\",\"Pending\")", registry);
        String fields = "values(Incident Number,Customer Login ID,Status,Priority,Description,Detailed Decription,Submit Date,Assignee Login ID,Assigned Group,Assignee,Resolution,Categorization Tier 1,Categorization Tier 2,Categorization Tier 3,Categorization Tier 4)";

        IncidentSearchResponse response = incidentClient.searchIncidents(query, fields);

        if (response != null && response.getEntries() != null) {
            return helixMapper.toIncidentResponseDtoList(response.getEntries());
        }

        return Collections.emptyList();
    }

    @Override
    public List<IncidentResponseDto> getAllTicketsByRegistry(String registry) {
        log.info("Buscando todos los tickets para: {}", registry);

        String query = String.format("'Customer Login ID'=\"%s\"", registry);
        String fields = "values(Incident Number,Customer Login ID,Status,Priority,Description,Detailed Decription,Submit Date,Assignee Login ID,Assigned Group,Assignee,Resolution,Categorization Tier 1,Categorization Tier 2,Categorization Tier 3,Categorization Tier 4)";

        IncidentSearchResponse response = incidentClient.searchIncidents(query, fields);

        if (response != null && response.getEntries() != null) {
            return helixMapper.toIncidentResponseDtoList(response.getEntries());
        }

        return Collections.emptyList();
    }
}
