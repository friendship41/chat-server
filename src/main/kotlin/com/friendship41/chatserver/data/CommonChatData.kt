package com.friendship41.chatserver.data

import java.util.*

data class CommonMessage(
        var messageId: Int,
        var memberNo: Int,
        var message: String,
        var createDate: Date?
)

data class CommonChatRoom(
        var roomId: String
)
