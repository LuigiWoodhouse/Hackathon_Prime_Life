package com.primelife.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final AppointmentWebSocketHandler appointmentWebSocketHandler;

    public WebSocketConfig(AppointmentWebSocketHandler appointmentWebSocketHandler) {
        this.appointmentWebSocketHandler = appointmentWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(appointmentWebSocketHandler, "/ws/appointments")
                .setAllowedOrigins("*");
    }
}
