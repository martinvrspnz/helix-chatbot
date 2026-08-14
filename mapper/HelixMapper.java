package com.bbva.chatbot.helix.mapper;

import com.bbva.chatbot.helix.dto.IncidentCreateResponse;
import com.bbva.chatbot.helix.dto.IncidentCreateResponseDto;
import com.bbva.chatbot.helix.dto.IncidentResponseDto;
import com.bbva.chatbot.helix.dto.IncidentSearchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface HelixMapper {

    @Mapping(target = "incidentNumber", source = "values", qualifiedByName = "mapIncidentNumber")
    IncidentCreateResponseDto toCreateResponseDto(IncidentCreateResponse response);

    @Mapping(target = "detailedDescription", source = "detailedDecription")
    IncidentResponseDto toIncidentResponseDto(IncidentSearchResponse.IncidentValues values);

    List<IncidentResponseDto> toIncidentResponseDtoList(List<IncidentSearchResponse.IncidentEntry> entries);

    default IncidentResponseDto toIncidentResponseDto(IncidentSearchResponse.IncidentEntry entry) {
        if (entry == null || entry.getValues() == null) {
            return null;
        }
        return toIncidentResponseDto(entry.getValues());
    }

    @Named("mapIncidentNumber")
    default String mapIncidentNumber(Map<String, String> values) {
        if (values == null) {
            return null;
        }
        return values.get("Incident Number");
    }
}
