package com.example.gamemate.domain.post.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Schema(description = "게임메이트 댓글 요청 DTO")
@Getter
public class PostCommentDTO {

    @Schema(description = "대댓글의 부모 아이디")
    private Long parentCommentId;

    @Schema(description = "댓글 내용")
    @NotNull
    @NotEmpty(message = "내용은 필수 입력 항목입니다.")
    private String content;
}