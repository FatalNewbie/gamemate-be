package com.example.gamemate.domain.chat.model.chatroommember;


import lombok.Getter;

@Getter
public class AddMemberResponse {
    private boolean success;
    private String content;

    public AddMemberResponse(boolean success, String content) {
        this.success = success;
        this.content = content;
    }

    public static AddMemberResponse from(boolean success, String content) {
        return new AddMemberResponse(success, content);
    }
}
