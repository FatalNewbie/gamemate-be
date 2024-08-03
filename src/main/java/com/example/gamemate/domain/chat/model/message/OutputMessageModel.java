package com.example.gamemate.domain.chat.model.message;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OutputMessageModel {

    // 스톰프 프레임에 맞게 변경.

    private Long id;
    private String writer;
    private Long chatRoomId;
    private String content;
    private String time;
    private MessageType type;


    public enum MessageType {
        CHAT, JOIN, LEAVE
    }


    public OutputMessageModel(Long id,String writer, Long chatRoomId, String content, String time, MessageType type) {
        this.id=id;
        this.writer = writer;
        this.chatRoomId = chatRoomId;
        this.content = content;
        this.time = time;
        this.type = type;
    }
}
