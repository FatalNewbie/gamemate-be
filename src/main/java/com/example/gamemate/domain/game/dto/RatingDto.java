package com.example.gamemate.domain.game.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class RatingDto {
    private Long id;
    private int rating;
    private String username;  // Assuming you'd want to display the username
}
