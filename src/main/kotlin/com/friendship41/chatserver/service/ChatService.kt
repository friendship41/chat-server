package com.friendship41.chatserver.service

import com.friendship41.chatserver.common.BusinessException
import com.friendship41.chatserver.common.CommonErrorCode
import com.friendship41.chatserver.data.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

const val NOINFO = "(NOINFO)"

@Service
class CommonChatService(@Autowired private val commonMessageRepository: CommonMessageRepository) {
    fun createCommonMessage(commonMessage: CommonMessage) {
        commonMessage.createDate = Date()
        commonMessageRepository.save(commonMessage)
    }

    fun getCommonMessageByMessageId(messageId: Int): CommonMessage = commonMessageRepository
            .findById(messageId)
            .orElseThrow { BusinessException(CommonErrorCode.INVALID_INPUT_VALUE.toErrorResponse()) }

    fun getCommonMessageByRoomId(roomId: Int): List<CommonMessage>
            = commonMessageRepository.findByCommonChatRoomRoomId(roomId)
}

@Service
class CommonChatRoomService(@Autowired private val commonMessageRepository: CommonMessageRepository,
                            @Autowired private val commonChatRoomRepository: CommonChatRoomRepository) {
    fun createCommonChatRoom(commonChatRoom: CommonChatRoom): CommonChatRoom {
        if (commonChatRoom.roomName == null) {
            commonChatRoom.roomName = NOINFO+UUID.randomUUID().toString().substring(NOINFO.length)
        }
        return commonChatRoomRepository.save(commonChatRoom)
    }

    fun getCommonChatRoomInfoByRoomId(roomId: Int): CommonChatRoom
            = commonChatRoomRepository.findById(roomId).orElseThrow{ BusinessException(ErrorResponse(
            "room not found by roomId=$roomId",
            400,
            null))}

    fun getCommonChatRoomInfoByMessageId(messageId: Int): CommonChatRoom = commonMessageRepository.findById(messageId)
            .orElseThrow{ BusinessException(ErrorResponse(
                    "message not found by messageId=$messageId",
                    400,
                    null)) }
            .commonChatRoom ?: throw BusinessException(
            ErrorResponse("room not found by messageId=$messageId", 400, null))

    fun deleteCommonChatRoom(roomId: Int)
            = commonChatRoomRepository.delete(CommonChatRoom(roomId, null, null))
}

