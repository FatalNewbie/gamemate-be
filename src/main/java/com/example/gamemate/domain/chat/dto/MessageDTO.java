package com.example.gamemate.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MessageDTO {
    private String content;
    private String writer;

    public MessageDTO(String content, String writer) {
        this.content = content;
        this.writer = writer;
    }
}
