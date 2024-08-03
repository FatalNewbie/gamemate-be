package com.example.gamemate.domain.game.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameListDto {
    private Long id;
    private Long userId;
    private Long gameId;
}
