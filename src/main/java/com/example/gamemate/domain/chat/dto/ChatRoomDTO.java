package com.example.gamemate.domain.chat.dto;

import lombok.Getter;

@Getter
public class ChatRoomDTO {
    private Long id;
    private String title;
    private String leaderNickname;
    private Long memberCnt;

    public ChatRoomDTO(Long id, String title, String leaderNickname, Long memberCnt) {
        this.id = id;
        this.title = title;
        this.leaderNickname = leaderNickname;
        this.memberCnt = memberCnt;
    }
}
