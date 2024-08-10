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

    public MessageModel(String writer,
                        String content,
                        Long chatRoomId) {
        this.writer = writer;
        this.content = content;
        this.chatRoomId = chatRoomId;
    }
}
