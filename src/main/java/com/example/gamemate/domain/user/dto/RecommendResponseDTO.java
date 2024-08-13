package com.example.gamemate.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendResponseDTO {
    @Schema(description = "추천 유저 이름")
    private String username;

    @Schema(description = "추천 유저와 공통되는 장르")
    private List<Long> preferredGenres;

    @Schema(description = "추천 유저와 공통되는 플레이 시간")
    private List<Long> playTimes;

    private String userProfile;
}