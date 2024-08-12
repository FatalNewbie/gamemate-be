package com.example.gamemate.domain.game.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GameCommentDto {
    private Long id;
    private Long userId;
    private Long gameId;
    private String content;
    private String username;
    private String nickname;  // 추가
    private LocalDateTime createdDate;  // 추가
}
