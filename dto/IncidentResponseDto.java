package com.bbva.chatbot.helix.dto;

import lombok.Data;

@Data
public class IncidentResponseDto {
    private String incidentNumber;
    private String customerLoginId;
    private String status;
    private String priority;
    private String description;
    private String detailedDescription;
    private String submitDate;
    private String assigneeLoginId;
    private String assignedGroup;
    private String assignee;
    private String resolution;
    private String categorizationTier1;
    private String categorizationTier2;
    private String categorizationTier3;
    private String categorizationTier4;
}
