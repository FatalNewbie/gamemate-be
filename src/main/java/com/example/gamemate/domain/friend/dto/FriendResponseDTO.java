package com.example.gamemate.domain.friend.dto;

import com.example.gamemate.domain.friend.entity.Friend;
import com.example.gamemate.domain.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendResponseDTO {

    private String message;
    private Friend.Status status;
    private UserDTO requester;
    private UserDTO receiver;
    private LocalDateTime acceptedDate;
}
