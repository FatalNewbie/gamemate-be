package com.example.gamemate.domain.post.dto;

import lombok.Getter;

@Getter
public class OnlinePostDTO extends PostDTO {

    public OnlinePostDTO() {
        super.setStatus("ON");
    }
}
