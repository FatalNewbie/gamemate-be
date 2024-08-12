package com.example.gamemate.domain.chat.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageModel {
    private String writer;
    private String content;
    private Long chatRoomId;
    private String type;

    public MessageModel(String writer, String content, Long chatRoomId, String type) {
        this.writer = writer;
        this.content = content;
        this.chatRoomId = chatRoomId;
        this.type = type;
    }
}
