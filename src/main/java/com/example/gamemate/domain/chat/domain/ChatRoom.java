package com.example.gamemate.domain.chat.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;



    @OneToMany(mappedBy= "chatRoom", cascade = CascadeType.ALL)
    private List<Message> messageList = new ArrayList<>();

    @OneToMany(mappedBy= "chatRoom", cascade = CascadeType.ALL)
    private List<ChatRoomMember> memberList = new ArrayList<>();

//    @ManyToOne
//    @JoinColumn(name = "leader")
//    private ChatTestUser leader;

    // 테스트용 유저.
    private String leader;

    public ChatRoom(String title, String leader) {

        this.title = title;
        this.leader = leader;
    }
}
