package com.example.gamemate.domain.chat.mapper;

import com.example.gamemate.domain.chat.entity.ChatRoom;
import com.example.gamemate.domain.chat.dto.ChatRoomDTO;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomMapper {
    public ChatRoomDTO convertoToChatRoomDTO(ChatRoom chatRoom){
        return new ChatRoomDTO(chatRoom.getId(),chatRoom.getTitle(), chatRoom.getLeader().getNickname(), chatRoom.getMemberCnt());
    }
}
