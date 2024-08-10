package com.example.gamemate.domain.friend.dto;

import com.example.gamemate.domain.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDTO {
    private UserDTO requester;
    private String status;
    private String createdDate;
}
