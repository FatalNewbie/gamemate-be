package com.example.gamemate.domain.chat.entity;

import com.example.gamemate.domain.post.entity.Post;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 채팅방 제목
    private String title;

    // 채팅방 정원
    private Long memberCnt;

    // 채팅메시지
    @OneToMany(mappedBy= "chatRoom", cascade = CascadeType.ALL)
    private List<Message> messageList = new ArrayList<>();

    // 채팅방 멤버
    @OneToMany(mappedBy= "chatRoom", cascade = CascadeType.ALL)
    private List<ChatRoomMember> memberList = new ArrayList<>();

    // 채팅방 방장
    @ManyToOne
    @JoinColumn(name = "leader")
    private User leader;

    // 채팅방 모집 게시글
    @OneToOne
    @JoinColumn(name = "post", unique = true)
    private Post post;


    public ChatRoom(String title, User leader, Long memberCnt, Post post) {

        this.title = title;
        this.leader = leader;
        this.memberCnt = memberCnt;
        this.post = post;
    }
}