package com.example.gamemate.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "게임메이트 댓글 응답 DTO")
@Builder
@Getter
public class PostCommentResponseDTO {

    @Schema(description = "유저 id")
    private Long id;

    @Schema(description = "유저 아이디")
    private String username;

    @Schema(description = "유저 닉네임")
    private String nickname;

    @Schema(description = "대댓글의 부모 아이디")
    private Long parentCommentId;

    @Schema(description = "유저 프로필 이미지")
    private String userProfile;

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "생성시간")
    private LocalDateTime createdDate;

    @Schema(description = "수정시간")
    private LocalDate modifiedDate;

    public void setCommentUsername(String username){
        this.username = username;
    }
}
