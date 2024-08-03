package com.example.gamemate.domain.chat.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class ChatRoomMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chatroomId")
    private ChatRoom chatRoom;

//    @ManyToOne
//    @JoinColumn(name = "member")
//    private ChatTestUser member;

    // 테스트용 유저.
    private String member;

    // 멤버가 리더인지 구분해주는 변수
    private boolean isLeader;


    public ChatRoomMember(ChatRoom chatRoom, String member, boolean isLeader) {
        this.chatRoom = chatRoom;
        this.member = member;
        this.isLeader = isLeader;
    }
}
