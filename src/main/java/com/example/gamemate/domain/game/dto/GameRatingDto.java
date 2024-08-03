package com.example.gamemate.domain.game.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameRatingDto {
    private Long id;
    private Long userId;
    private Long gameId;
    private int rating;
}
