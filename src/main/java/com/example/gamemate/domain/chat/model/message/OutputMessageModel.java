package com.example.gamemate.domain.chat.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class OutputMessageModel {

    // 스톰프 프레임에 맞게 변경.

    private Long id;
    private String writer;
    private Long chatRoomId;
    private String content;
    private String time;
    private String type;
    private Long writerId;
    private String writerProfile;

    public enum MessageType {
        CHAT, JOIN, LEAVE, INVITE
    }



    public OutputMessageModel(Long id, String writer, Long chatRoomId, String content, String time, String type, Long writerId, String writerProfile) {
        this.id = id;
        this.writer = writer;
        this.chatRoomId = chatRoomId;
        this.content = content;
        this.time = time;
        this.type = type;
        this.writerId = writerId;
        this.writerProfile = writerProfile;
    }
}
