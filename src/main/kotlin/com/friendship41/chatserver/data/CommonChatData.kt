package com.friendship41.chatserver.data

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "messageId")
data class CommonMessage(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var messageId: Int?,
        var memberNo: Int?,
        var message: String,
        var createDate: Date?,
        @ManyToOne
        @JoinColumn(name = "ROOM_ID")
        var commonChatRoom: CommonChatRoom?
)

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "roomId")
data class CommonChatRoom(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var roomId: Int?,
        @OneToMany(mappedBy = "commonChatRoom")
        var commonMessage: List<CommonMessage>?
)

@Repository
interface CommonMessageRepository: JpaRepository<CommonMessage, Int> {

}

@Repository
interface CommonChatRoomRepository: JpaRepository<CommonChatRoom, Int> {

}
