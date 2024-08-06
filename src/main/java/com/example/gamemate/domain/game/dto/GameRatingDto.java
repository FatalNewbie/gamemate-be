package com.example.gamemate.domain.game.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class GameRatingDto {
    private Long id;
    private Long userId;
    private Long gameId;
    private int rating;
}
