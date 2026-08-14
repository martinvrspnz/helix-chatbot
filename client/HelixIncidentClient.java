package com.bbva.chatbot.helix.client;

import com.bbva.chatbot.helix.dto.IncidentCreateRequest;
import com.bbva.chatbot.helix.dto.IncidentCreateResponse;
import com.bbva.chatbot.helix.dto.IncidentSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "helix-incident-client", url = "${helix.api.url}/api/arsys/v1")
public interface HelixIncidentClient {


    @PostMapping("/entry/HPD:IncidentInterface_Create")
    IncidentCreateResponse createIncident(
            @RequestBody IncidentCreateRequest request,
            @RequestParam("fields") String fields
    );



    @GetMapping("/entry/HPD:Help%20Desk")
    IncidentSearchResponse searchIncidents(
            @RequestParam("q") String query,
            @RequestParam("fields") String fields
    );
}