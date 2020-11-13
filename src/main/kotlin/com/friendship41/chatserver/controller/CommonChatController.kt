package com.friendship41.chatserver.controller

import com.friendship41.chatserver.data.CommonChatRoomRepository
import com.friendship41.chatserver.data.CommonMessage
import com.friendship41.chatserver.data.CommonMessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CommonChatController(
        @Autowired private val simpMessagingTemplate: SimpMessagingTemplate,
        @Autowired private val commonMessageRepository: CommonMessageRepository,
        @Autowired private val commonChatRoomRepository: CommonChatRoomRepository) {
    @MessageMapping("/common")
    fun sendCommonMessage(commonMessage: CommonMessage) {
        simpMessagingTemplate.convertAndSend("/sub/qwe", commonMessage)
    }

    @GetMapping("qwe")
    fun testGet(): Any? {
        val a =commonMessageRepository.findById(3)
        val b = commonChatRoomRepository.findById(1)
        return a
    }
}

