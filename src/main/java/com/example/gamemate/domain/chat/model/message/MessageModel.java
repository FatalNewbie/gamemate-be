package com.example.gamemate.domain.chat.model.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageModel {
    private String writer;
    private String content;
    private Long chatRoomId;

    public MessageModel(String writer, String content, Long chatRoomId) {
        this.writer = writer;
        this.content = content;
        this.chatRoomId = chatRoomId;
    }
}
