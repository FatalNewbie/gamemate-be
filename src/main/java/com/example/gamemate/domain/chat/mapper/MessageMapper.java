package com.example.gamemate.domain.chat.mapper;

import com.example.gamemate.domain.chat.domain.Message;
import com.example.gamemate.domain.chat.dto.MessageDTO;

public class MessageMapper {
    public static MessageDTO toDTO(Message message) {
        if (message == null) {
            return null;
        }
        MessageDTO messageDTO = new MessageDTO();

        messageDTO.setContent(message.getContent());
        messageDTO.setWriter(message.getWriter().getNickname());

        // 필요한 다른 필드들도 설정
        return messageDTO;
    }
}
