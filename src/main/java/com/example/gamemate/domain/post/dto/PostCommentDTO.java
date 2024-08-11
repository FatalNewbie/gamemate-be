package com.example.gamemate.domain.post.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class PostCommentDTO {

    private Long parentCommentId;

    @NotNull
    @NotEmpty(message = "내용은 필수 입력 항목입니다.")
    private String content;
}