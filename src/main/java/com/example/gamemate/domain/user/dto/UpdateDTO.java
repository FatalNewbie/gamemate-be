package com.example.gamemate.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UpdateDTO {

    private Long id;
    private String username;
    private String password;
    private String nickname;

}
