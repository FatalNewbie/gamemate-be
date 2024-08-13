package com.example.gamemate.domain.chat.mapper;

import com.example.gamemate.domain.chat.entity.Message;
import com.example.gamemate.domain.chat.model.message.OutputMessageModel;

public class MessageMapper {
    public static OutputMessageModel toOutputMessageModel(Message message) {
        if (message == null) {
            return null;
        }
        OutputMessageModel outputMessageModel =OutputMessageModel.builder()
                .id(message.getId())
                .content(message.getContent())
                .writer(message.getWriter().getNickname())
                .chatRoomId(message.getChatRoom().getId())
                .time(message.getTime())
                .type(message.getType())
                .writerId(message.getWriter().getId())
                .build();

        // 필요한 다른 필드들도 설정
        return outputMessageModel;
    }
}
