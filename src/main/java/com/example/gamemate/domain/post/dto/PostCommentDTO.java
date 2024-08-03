package com.example.gamemate.domain.post.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class PostCommentDTO {

    @JsonProperty("pCommentId")
    private Long pCommentId;

    @NotNull
    private String content;
}