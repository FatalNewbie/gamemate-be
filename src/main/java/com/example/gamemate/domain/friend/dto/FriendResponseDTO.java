package com.example.gamemate.domain.friend.dto;

import com.example.gamemate.domain.friend.entity.Friend;
import com.example.gamemate.domain.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "결과 메시지", example = "친구 요청을 수락하였습니다.")
    private String message;

    @Schema(description = "친구 요청 상태", example = "ACCEPTED or REJECTED or PENDING or CANCELED")
    private Friend.Status status;

    @Schema(description = "친구 요청을 보낸 사용자")
    private UserDTO requester;

    @Schema(description = "친구 요청을 받은 사용자")
    private UserDTO receiver;

    @Schema(description = "친구가 된 시점", example = "2023-01-01T00:00:00")
    private LocalDateTime acceptedDate;
}
