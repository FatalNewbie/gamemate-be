package com.example.gamemate.domain.chat.dto;

import lombok.Getter;

@Getter
public class ChatRoomDTO {
    private Long id;
    private String title;
    private String leader;

    public ChatRoomDTO(Long id, String title, String leader) {
        this.id = id;
        this.title = title;
        this.leader = leader;
    }
}
