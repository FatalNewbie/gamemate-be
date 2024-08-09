package com.example.gamemate.domain.chat.dto;

import com.example.gamemate.domain.chat.domain.ChatRoom;
import com.example.gamemate.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Builder
public class MessageDTO {
    private Long id;
    private String content;
    private String writer;
    private Long chatRoomId;
    private String time;
    private String type;



    public MessageDTO(Long id, String content, String writer, Long chatRoomId, String time, String type) {
        this.id = id;
        this.content = content;
        this.writer = writer;
        this.chatRoomId = chatRoomId;
        this.time = time;
        this.type = type;
    }

}
