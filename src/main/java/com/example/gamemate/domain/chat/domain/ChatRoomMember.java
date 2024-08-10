package com.example.gamemate.domain.chat.domain;

import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class ChatRoomMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chatroomId")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "member")
    private User member;

    // 테스트용 유저.


    // 멤버가 리더인지 구분해주는 변수
    private boolean isLeader;


    public ChatRoomMember(ChatRoom chatRoom, User member, boolean isLeader) {
        this.chatRoom = chatRoom;
        this.member = member;
        this.isLeader = isLeader;
    }
}
