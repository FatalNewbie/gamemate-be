package com.example.gamemate.domain.game.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameCommentDto {
    private Long id;
    private Long userId;
    private Long gameId;
    private String content;
}
