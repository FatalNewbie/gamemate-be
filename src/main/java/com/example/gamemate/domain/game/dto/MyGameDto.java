package com.example.gamemate.domain.game.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyGameDto {
    private Long id;
    private Long userId;
    private String username;
    private GameDto game;
}
