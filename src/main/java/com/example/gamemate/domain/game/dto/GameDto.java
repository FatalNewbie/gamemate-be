package com.example.gamemate.domain.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {
    private Long id;
    private String title;
    private String developer;
    private String description;
    private String classes;
    private String genre;
    private String platform;
}
