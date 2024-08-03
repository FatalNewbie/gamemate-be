package com.example.gamemate.domain.chat.model.chatroom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomCreateRequest {
    private String chatTitle;
    private String user;

    public ChatRoomCreateRequest(String chatTitle, String user) {
        this.chatTitle = chatTitle;
        this.user = user;
    }
}
