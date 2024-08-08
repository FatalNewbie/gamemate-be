package com.example.gamemate.domain.game.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@Builder
public class GameDto {
    private Long id;
    private String title;
    private String developer;
    private String description;
    private String classes;
    private String genre;
    private String platform;
    private List<CommentDto> comments; // Added list of comments
    private List<RatingDto> ratings;   // Added list of ratings
}
