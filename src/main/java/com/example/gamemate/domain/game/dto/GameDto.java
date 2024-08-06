package com.example.gamemate.domain.game.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Builder
public class GameDto {
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String developer;

    @NotNull
    private String description;

    @NotNull
    private String classes;

    @NotNull
    private String genre;

    @NotNull
    private String platform;
}
