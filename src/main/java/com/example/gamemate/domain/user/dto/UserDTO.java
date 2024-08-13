package com.example.gamemate.domain.user.dto;

import com.example.gamemate.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

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
    private String userProfile;
    private List<String> preferredGenres;
    private List<String> playTimes;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.role = user.getRole().name();
        this.deleted = user.isDeleted();
        this.userProfile = user.getUserProfile();
        this.preferredGenres = user.getPreferredGenres().stream().map(genre -> genre.getName()).collect(Collectors.toList());
        this.playTimes = user.getPlayTimes().stream().map(playtime -> playtime.getTimeSlot()).collect(Collectors.toList());
    }
}