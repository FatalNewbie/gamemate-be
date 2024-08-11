package com.example.gamemate.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class PostCommentsResponseDTO {

    private Long id;

    private String username;

    private String nickname;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private List<RecommentsResponseDTO> recomments;
}
