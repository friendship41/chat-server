package com.friendship41.chatserver.controller

import com.friendship41.chatserver.data.CommonMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.RestController

@RestController
class CommonChatController(
        @Autowired private val simpMessagingTemplate: SimpMessagingTemplate) {
    @MessageMapping("/common")
    fun sendCommonMessage(commonMessage: CommonMessage) {
        simpMessagingTemplate.convertAndSend("/sub/qwe", commonMessage)
    }
}

