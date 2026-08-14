package com.bbva.chatbot.helix.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class IncidentSearchResponse {

    private List<IncidentEntry> entries;

    @Data
    public static class IncidentEntry {
        private IncidentValues values;
    }

    @Data
    public static class IncidentValues {
        @JsonProperty("Incident Number")
        private String incidentNumber;

        @JsonProperty("Customer Login ID")
        private String customerLoginId;

        @JsonProperty("Status")
        private String status;

        @JsonProperty("Priority")
        private String priority;

        @JsonProperty("Description")
        private String description;

        @JsonProperty("Detailed Decription")
        private String detailedDecription;

        @JsonProperty("Submit Date")
        private String submitDate;

        @JsonProperty("Assignee Login ID")
        private String assigneeLoginId;

        @JsonProperty("Assigned Group")
        private String assignedGroup;

        @JsonProperty("Assignee")
        private String assignee;

        @JsonProperty("Resolution")
        private String resolution;

        @JsonProperty("Categorization Tier 1")
        private String categorizationTier1;

        @JsonProperty("Categorization Tier 2")
        private String categorizationTier2;

        @JsonProperty("Categorization Tier 3")
        private String categorizationTier3;

        @JsonProperty("Categorization Tier 4")
        private String categorizationTier4;
    }
}