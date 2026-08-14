package com.bbva.chatbot.helix.controller;

import com.bbva.chatbot.helix.dto.CreateIncidentRequest;
import com.bbva.chatbot.helix.dto.IncidentCreateResponseDto;
import com.bbva.chatbot.helix.dto.IncidentResponseDto;
import com.bbva.chatbot.helix.service.HelixService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/helix")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HelixController {

    private final HelixService helixService;

    @PostMapping("/tickets")
    public ResponseEntity<IncidentCreateResponseDto> createIncident(@RequestBody CreateIncidentRequest request) {
        var response = helixService.createIncident(request.registry(), request.title(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<IncidentResponseDto> getIncidentDetails(@PathVariable String ticketId) {
        var details = helixService.getIncidentDetails(ticketId);
        if (details == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(details);
    }

    @GetMapping("/tickets/email")
    public ResponseEntity<List<IncidentResponseDto>> getMyTicketsByEmail(@RequestParam String email) {
        var tickets = helixService.getMyTicketsByEmail(email);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/tickets/registry/active")
    public ResponseEntity<List<IncidentResponseDto>> getActiveTicketsByRegistry(@RequestParam String registry) {
        var tickets = helixService.getActiveTicketsByRegistry(registry);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/tickets/registry/all")
    public ResponseEntity<List<IncidentResponseDto>> getAllTicketsByRegistry(@RequestParam String registry) {
        var tickets = helixService.getAllTicketsByRegistry(registry);
        return ResponseEntity.ok(tickets);
    }
}
