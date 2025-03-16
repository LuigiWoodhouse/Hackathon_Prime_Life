package com.primelife.service.impl;

import com.azure.json.implementation.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.primelife.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void notifyUsers(String jsonPayload) {
        log.trace("Enter Method notifyUsers :{} ", jsonPayload);

        try {
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode node = mapper.readTree(jsonPayload);
//
//            String accessToken = node.get("accessToken").asText();
//            JsonNode targetUsersNode = node.get("targetUsers");
//            String message = node.get("message").asText();

//            String validatedAccessToken = tokenManagementService.validateAccessToken(accessToken);
//            Map<String, String> userDetails = tokenManagementService.fetchUserDetailsFromAccessToken(validatedAccessToken);
//            String senderId = userDetails.get("userId");
//
//            if (targetUsersNode.isArray()) {
//                for (JsonNode targetUserNode : targetUsersNode) {
//                    String targetUserId = targetUserNode.asText();
//
//                    ObjectNode responseNode = mapper.createObjectNode();
//                    responseNode.put("senderId", senderId);
//                    responseNode.put("recipientId", targetUserId);
//                    responseNode.put("message", message);

//                    String responseJson = mapper.writeValueAsString(responseNode);

//                    simpMessagingTemplate.convertAndSendToUser(targetUserId, "/queue/notifications", responseJson);
//                    log.info("Exit Method notifyUsers: Notification sent from {} to {}", senderId, targetUserId);
//                }
//            }
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

}
