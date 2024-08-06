package com.example.gamemate.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JoinDTO {

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Size(max = 50, message = "이메일은 최대 50자까지 입력 가능합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
//    @Size(min = 8, max = 100, message = "비밀번호는 최소 8자, 최대 100자까지 입력 가능합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @Size(max = 20, message = "닉네임은 최대 20자까지 입력 가능합니다.")
    private String nickname;


    private List<Integer> preferredGenres;  // 이진 인코딩된 선호 장르
    private List<Integer> playTimes;        // 이진 인코딩된 플레이 시간대
}
