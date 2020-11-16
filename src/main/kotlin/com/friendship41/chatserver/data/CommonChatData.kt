package com.friendship41.chatserver.data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "messageId")
data class CommonMessage(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @JsonView
        var messageId: Int?,
        var memberNo: Int?,
        var message: String,
        var createDate: Date?,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "ROOM_ID", updatable = false)
        var commonChatRoom: CommonChatRoom?
)

data class MessageWithTarget(
        var commonMessage: CommonMessage,
        var targetMemberNoList: List<Int>
)

@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "roomId")
data class CommonChatRoom(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var roomId: Int?,
        var roomName: String?,
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "commonChatRoom", cascade = [CascadeType.ALL])
        @JsonIgnore
        var commonMessage: List<CommonMessage>?
)

@Repository
interface CommonMessageRepository: JpaRepository<CommonMessage, Int> {
        fun findByCommonChatRoomRoomId(roomId: Int): List<CommonMessage>
}

@Repository
interface CommonChatRoomRepository: JpaRepository<CommonChatRoom, Int>
