package com.example.gamemate.domain.chat.model.chatroommember;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddMemberRequest {
    private Long chatRoomId;
    private String addMemberUsername;



    public AddMemberRequest(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public AddMemberRequest(Long chatRoomId, String addMemberUsername) {
        this.chatRoomId = chatRoomId;
        this.addMemberUsername = addMemberUsername;
    }
}


