package com.friendship41.chatserver.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig: WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        // subscribe prefix
        registry.enableSimpleBroker("/sub")
        // publish prefix
        registry.setApplicationDestinationPrefixes("/pub")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/connect").setAllowedOrigins("*").withSockJS()
    }
}
