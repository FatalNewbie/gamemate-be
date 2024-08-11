package com.example.gamemate.domain.post.dto;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class RecommentsResponseDTO {

    private Long id;

    private String username;

    private String nickname;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}
