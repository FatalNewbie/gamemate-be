package com.example.gamemate.domain.chat.repository;

import com.example.gamemate.domain.chat.entity.ChatRoom;
import com.example.gamemate.domain.chat.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;


public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChatRoom(ChatRoom chatRoom);

    @Query("SELECT m FROM Message m WHERE m.chatRoom.id = :chatRoomId AND m.id < :lastMessageId ORDER BY m.id DESC")
    List<Message> findMessagesByChatRoomIdAndBeforeMessageId(@Param("chatRoomId") Long chatRoomId, @Param("lastMessageId") Long lastMessageId, Pageable pageable);


}
