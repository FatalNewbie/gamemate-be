package com.example.gamemate.domain.chat.model.chatroom;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomCreateRequest {
    private String chatTitle;


    private Long memberCnt;

    public ChatRoomCreateRequest(String chatTitle, Long memberCnt) {
        this.chatTitle = chatTitle;

        this.memberCnt = memberCnt;
    }
}
