package com.example.gamemate.domain.chat.model.chatroom;

import lombok.Getter;

@Getter
public class ChatRoomCreateResponse {
    private boolean success;
    private String content;

    public ChatRoomCreateResponse(boolean success, String content) {
        this.success = success;
        this.content = content;
    }

    public static ChatRoomCreateResponse from(boolean success, String content) {
        return new ChatRoomCreateResponse(success, content);
    }
}
