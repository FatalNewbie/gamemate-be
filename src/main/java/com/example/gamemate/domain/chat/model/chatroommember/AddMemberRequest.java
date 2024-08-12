package com.example.gamemate.domain.chat.model.chatroommember;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddMemberRequest {
    private Long chatRoomId;
    private Long writerId;



    public AddMemberRequest(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public AddMemberRequest(Long chatRoomId, Long writerId) {
        this.chatRoomId = chatRoomId;
        this.writerId = writerId;
    }
}


