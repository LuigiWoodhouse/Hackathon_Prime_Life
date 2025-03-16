package com.primelife.controller;

import com.primelife.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class NotifyController {

    @Autowired
    NotificationService notificationService;

    @MessageMapping("/notify")
    public void sendNotification(String jsonPayload) throws Exception {

        notificationService.notifyUsers(jsonPayload);
    }
}
