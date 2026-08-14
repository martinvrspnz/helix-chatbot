package com.bbva.chatbot.helix.config;

import com.bbva.chatbot.helix.dto.IncidentCreateResponseDto;
import com.bbva.chatbot.helix.dto.IncidentResponseDto;
import com.bbva.chatbot.helix.service.HelixService;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import com.bbva.chatbot.helix.util.UserContextHolder;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class ChatToolsConfig {

    private final HelixService helixService;

    // 1. REGISTRO/CREACIÓN DE INCIDENTE
    public record CreateIncidentRequest(
            @JsonPropertyDescription("El registro único del empleado (ej. usuario o id de registro)") String registry,
            @JsonPropertyDescription("El título corto del incidente o problema") String title,
            @JsonPropertyDescription("La descripción detallada del problema que experimenta el usuario") String description) {
    }

    // 2. CONSULTA DE DETALLES DE INCIDENTE
    public record IncidentDetailsRequest(
            @JsonPropertyDescription("El identificador único o número del ticket (ej. INC000001234)") String ticketId) {
    }

    // 3. OBTENCIÓN DE MIS TICKETS
    public record MyTicketsByRegistryRequest(
            @JsonPropertyDescription("El registro único del empleado para buscar sus tickets asignados o reportados") String registry) {
    }

    @Bean
    @Description("Registra o crea un nuevo incidente/ticket de soporte técnico en Helix cuando el usuario reporta un fallo o problema.")
    public Function<CreateIncidentRequest, IncidentCreateResponseDto> createIncidentTool() {
        return request -> {
            String registry = UserContextHolder.getUsuarioId();
            if (registry == null || registry.trim().isEmpty()) {
                registry = request.registry();
            }
            return helixService.createIncident(
                    registry,
                    request.title(),
                    request.description());
        };
    }

    @Bean
    @Description("""
                Obtiene los detalles, estado, prioridad y asignación de un incidente específico a partir de su ID o número de ticket.
                Esta herramienta retorna un objeto con los siguientes campos clave que debes interpretar para el usuario:
                - 'incidentNumber': El código identificador del ticket.
                - 'status': El estado actual (ej. 'Assigned' significa asignado, 'In Progress' significa en revisión, 'Resolved' que ya está solucionado).
                - 'priority': Qué tan urgente es (Critical, High, Medium, Low).
                - 'description': El título o resumen del fallo.
                - 'detailedDescription': Todo el detalle técnico y notas del problema.
                - 'assignedGroup': El equipo resolutor de BBVA a cargo.
                - 'assignee': El especialista asignado.
            """)
    public Function<IncidentDetailsRequest, IncidentResponseDto> getIncidentDetailsTool() {
        return request -> helixService.getIncidentDetails(request.ticketId());
    }

    @Bean
    @Description("Consulta la lista de todos los tickets o incidentes reportados por un empleado a partir de su registro, codigo de epleado o usuarioId.")
    public Function<MyTicketsByRegistryRequest, List<IncidentResponseDto>> getMyTicketsByRegistryTool() {
        return request -> {
            String registry = UserContextHolder.getUsuarioId();
            if (registry == null || registry.trim().isEmpty()) {
                registry = request.registry();
            }
            return helixService.getActiveTicketsByRegistry(registry);
        };
    }
}
