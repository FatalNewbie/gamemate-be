package com.example.gamemate.domain.chat.mapper;

import com.example.gamemate.domain.chat.domain.Message;
import com.example.gamemate.domain.chat.dto.MessageDTO;

public class MessageMapper {
    public static MessageDTO toDTO(Message message) {
        if (message == null) {
            return null;
        }
        MessageDTO messageDTO =MessageDTO.builder()
                .id(message.getId())
                .content(message.getContent())
                .writer(message.getWriter().getNickname())
                .chatRoomId(message.getChatRoom().getId())
                .time(message.getTime())
                .type(message.getType())
                .build();

        // 필요한 다른 필드들도 설정
        return messageDTO;
    }
}
