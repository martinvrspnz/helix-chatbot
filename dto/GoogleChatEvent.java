package com.bbva.chatbot.helix.dto;

import java.util.List;
import java.util.Map;

public record GoogleChatEvent(
        String type,
        String displayName,
        String email,
        String text,
        String actionMethodName,
        Map<String, Object> body
) {

    @SuppressWarnings("unchecked")
    public static GoogleChatEvent parse(Map<String, Object> body) {
        String type = (String) body.get("type");
        String displayName = "";
        String email = "";
        String text = "";
        String actionMethodName = null;

        if ("ADDED_TO_SPACE".equals(type)) {
            Map<String, Object> userMap = (Map<String, Object>) body.get("user");
            if (userMap != null) {
                displayName = (String) userMap.get("displayName");
                email = (String) userMap.get("email");
            }
            text = "hola";
        } else if ("MESSAGE".equals(type)) {
            Map<String, Object> messageMap = (Map<String, Object>) body.get("message");
            if (messageMap != null) {
                text = (String) messageMap.get("text");
                Map<String, Object> senderMap = (Map<String, Object>) messageMap.get("sender");
                if (senderMap != null) {
                    displayName = (String) senderMap.get("displayName");
                    email = (String) senderMap.get("email");
                }
            }
        } else if ("CARD_CLICKED".equals(type)) {
            Map<String, Object> userMap = (Map<String, Object>) body.get("user");
            if (userMap != null) {
                displayName = (String) userMap.get("displayName");
                email = (String) userMap.get("email");
            }
            Map<String, Object> actionMap = (Map<String, Object>) body.get("action");
            if (actionMap != null) {
                actionMethodName = (String) actionMap.get("actionMethodName");
            }
        }

        return new GoogleChatEvent(type, displayName, email, text, actionMethodName, body);
    }

    @SuppressWarnings("unchecked")
    public String getFormInputValue(String fieldName) {
        try {
            Map<String, Object> common = (Map<String, Object>) body.get("common");
            if (common == null) return null;

            Map<String, Object> formInputs = (Map<String, Object>) common.get("formInputs");
            if (formInputs == null) return null;

            Map<String, Object> field = (Map<String, Object>) formInputs.get(fieldName);
            if (field == null) return null;

            Map<String, Object> stringInputs = (Map<String, Object>) field.get("stringInputs");
            if (stringInputs == null) return null;

            List<String> values = (List<String>) stringInputs.get("value");

            return (values != null && !values.isEmpty()) ? values.getFirst() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
