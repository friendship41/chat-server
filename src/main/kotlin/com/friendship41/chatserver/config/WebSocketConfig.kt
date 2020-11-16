package com.friendship41.chatserver.config

import com.friendship41.chatserver.common.BusinessException
import com.friendship41.chatserver.common.CommonErrorCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(@Autowired private val stompAuthHandler: StompAuthHandler): WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        // subscribe prefix
        registry.enableSimpleBroker("/sub")
        // publish prefix
        registry.setApplicationDestinationPrefixes("/pub")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/connect").setAllowedOrigins("*").withSockJS()
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(this.stompAuthHandler)
    }
}

@Component
class StompAuthHandler(@Autowired private val tokenProvider: JwtTokenProvider): ChannelInterceptor {
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = StompHeaderAccessor.wrap(message)

        if (StompCommand.CONNECT == accessor.command) {
            tokenProvider.validateJwt(accessor.getFirstNativeHeader("access_token")
                    ?: throw BusinessException(CommonErrorCode.BAD_CREDENTIALS.toErrorResponse()))
        }
        return message
    }
}
