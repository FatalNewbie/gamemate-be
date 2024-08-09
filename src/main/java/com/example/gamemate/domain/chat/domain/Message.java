package com.example.gamemate.domain.chat.domain;

import com.example.gamemate.domain.chat.model.message.OutputMessageModel;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
public class Message extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String time;

    private String type;

    @ManyToOne
    @JoinColumn(name = "chatroomId")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "writer")
    private User writer;

    // 테스트용 유저.
//    private User writer;

    public Message(String content, ChatRoom chatRoom, User writer, String time, String type) {
        this.content = content;
        this.chatRoom = chatRoom;
        this.writer = writer;
        this.time =time;
        this.type = type;

    }
}
