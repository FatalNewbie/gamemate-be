package com.example.gamemate.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "온라인 게시글 요청 DTO")
@Getter
public class OnlinePostDTO extends PostDTO {

    public OnlinePostDTO() {
        super.setStatus("ON");
    }
}
