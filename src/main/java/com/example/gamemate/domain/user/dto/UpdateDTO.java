package com.example.gamemate.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UpdateDTO {

    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String userProfile;

    @Schema(description = "이진 인코딩된 선호 장르 리스트")
    private List<Integer> preferredGenres;
    @Schema(description = "이진 인코딩된 플레이 시간대 리스트")
    private List<Integer> playTimes;
}
