package com.example.gamemate.domain.chat.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "chatroomId")
    private ChatRoom chatRoom;



//    @ManyToOne
//    @JoinColumn(name = "writer")
//    private ChatTestUser writer;

    // 테스트용 유저.
    private String writer;

    public Message(String content, ChatRoom chatRoom, String writer) {
        this.content = content;
        this.chatRoom = chatRoom;
        this.writer = writer;
    }
}
