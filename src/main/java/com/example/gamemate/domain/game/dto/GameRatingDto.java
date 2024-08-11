package com.example.gamemate.domain.game.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
@Getter
public class GameRatingDto {
    private Long id;
    private Long userId;
    private String username;
    private Long gameId;
    private int rating;
}
