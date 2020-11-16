package com.friendship41.chatserver.controller

import com.friendship41.chatserver.common.BusinessException
import com.friendship41.chatserver.common.CommonErrorCode
import com.friendship41.chatserver.data.CommonChatRoom
import com.friendship41.chatserver.data.CommonMessage
import com.friendship41.chatserver.data.ErrorResponse
import com.friendship41.chatserver.data.MessageWithTarget
import com.friendship41.chatserver.service.CommonChatRoomService
import com.friendship41.chatserver.service.CommonChatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.*

@RestController
class CommonChatController(
        @Autowired private val simpMessagingTemplate: SimpMessagingTemplate,
        @Autowired private val commonChatService: CommonChatService) {

    // config에 의해 /pub이 붙음
    // @Header(value = "access_token", required = false) accessToken: String? 헤더로 토큰 받는법
    @MessageMapping("/room")
    fun publishCommonMessageToRoom(commonMessage: CommonMessage) {
        if (commonMessage.commonChatRoom == null || commonMessage.commonChatRoom!!.roomId == null) {
            throw BusinessException(ErrorResponse(
                    "no roomId",
                    400,
                    null))
        }
        commonChatService.createCommonMessage(commonMessage)
        simpMessagingTemplate.convertAndSend("/sub/room/${commonMessage.commonChatRoom!!.roomId}", commonMessage)
    }

    // config에 의해 /pub이 붙음
    @MessageMapping("/member")
    fun publishCommonMessageToMember(messageWithTarget: MessageWithTarget) {
        for (targetMemberNo: Int in messageWithTarget.targetMemberNoList) {
            simpMessagingTemplate.convertAndSend("/sub/member/$targetMemberNo", messageWithTarget.commonMessage)
        }
    }

    @GetMapping("/message")
    fun getMessage(@RequestParam(required = false) messageId: Int?,
                   @RequestParam(required = false) roomId: Int?): Any {
        return when {
            messageId != null -> commonChatService.getCommonMessageByMessageId(messageId)
            roomId != null -> commonChatService.getCommonMessageByRoomId(roomId)
            else -> throw BusinessException(CommonErrorCode.INVALID_INPUT_VALUE.toErrorResponse())
        }
    }
}

@RestController
@RequestMapping("/room")
class CommonChatRoomController(@Autowired private val commonChatRoomService: CommonChatRoomService) {
    @PostMapping
    fun createChatRoom(@RequestBody commonChatRoom: CommonChatRoom): CommonChatRoom
            = commonChatRoomService.createCommonChatRoom(commonChatRoom)

    @GetMapping
    fun getChatRoom(@RequestParam(required = false) roomId: Int?,
                    @RequestParam(required = false) messageId: Int?): CommonChatRoom = when {
        roomId != null -> commonChatRoomService.getCommonChatRoomInfoByRoomId(roomId)
        messageId != null -> commonChatRoomService.getCommonChatRoomInfoByMessageId(messageId)
        else -> throw BusinessException(CommonErrorCode.INVALID_INPUT_VALUE.toErrorResponse())
    }

    @DeleteMapping
    fun deleteChatRoom(@RequestParam roomId: Int) {
        commonChatRoomService.deleteCommonChatRoom(roomId)
    }
}

