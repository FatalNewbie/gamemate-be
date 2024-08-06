package com.example.gamemate.domain.chat.model.chatroommember;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequest {
    private Long chatRoomId;


    public AddMemberRequest(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
}


