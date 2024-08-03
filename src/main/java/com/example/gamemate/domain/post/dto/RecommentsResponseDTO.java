package com.example.gamemate.domain.post.dto;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RecommentsResponseDTO {

    private Long id;

    private String nickname;

    private String content;
}
