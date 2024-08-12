package com.example.gamemate.domain.friend.dto;

import com.example.gamemate.domain.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDTO {
    @Schema(description = "친구 요청을 보낸 사용자")
    private UserDTO requester;

    @Schema(description = "친구 요청 상태", example = "ACCEPTED or REJECTED or PENDING or CANCELED")
    private String status;

    @Schema(description = "친구 요청 시점", example = "2023-01-01T00:00:00")
    private String createdDate;
}
