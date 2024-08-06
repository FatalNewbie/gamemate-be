package com.example.gamemate.domain.friend.dto;

import com.example.gamemate.domain.friend.entity.Friend;
import com.example.gamemate.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendResponseDTO {

    private String message;
    private Friend.Status status;
    private User requester;
    private User receiver;

    public FriendResponseDTO(String message) {
        this.message = message;
    }
}
