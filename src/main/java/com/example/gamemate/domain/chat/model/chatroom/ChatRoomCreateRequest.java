package com.example.gamemate.domain.chat.model.chatroom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomCreateRequest {
    private String chatTitle;
    private String user;

    private Long memberCnt;

    public ChatRoomCreateRequest(String chatTitle, String user, Long memberCnt) {
        this.chatTitle = chatTitle;
        this.user = user;
        this.memberCnt = memberCnt;
    }
}
