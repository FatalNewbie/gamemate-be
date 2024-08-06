package com.example.gamemate.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String nickname;
    private String role;
    private boolean deleted;
    private List<String> preferredGenres;
    private List<String> playTimes;
}