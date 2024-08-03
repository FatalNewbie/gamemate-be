package com.example.gamemate.domain.chat.repository;

import com.example.gamemate.domain.chat.domain.ChatRoom;
import com.example.gamemate.domain.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChatRoom(ChatRoom chatRoom);
}
