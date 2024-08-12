package com.example.gamemate.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Schema(description = "게임메이드 댓글 수정 응답 DTO")
@AllArgsConstructor
@Getter
public class PostUpdateResponseDTO {

    @Schema(description = "유저 아이디")
    private String username;

    @Schema(description = "유저 닉네임")
    private String nickname;

    @Schema(description = "온/오프 상태값")
    private String status;

    @Schema(description = "게시글 제목")
    private String gameTitle;

    @Schema(description = "게임 장르")
    private String gameGenre;

    @Schema(description = "모집 인원 수")
    private Long mateCnt;

    @Schema(description = "게시글 내용")
    private String mateContent;

    @Schema(description = "댓글 개수")
    private Long commentCnt;

    @Schema(description = "지역(시)")
    private String mateRegionSi;

    @Schema(description = "지역(구)")
    private String mateRegionGu;

    @Schema(description = "위도")
    private BigDecimal latitude;

    @Schema(description = "경도")
    private BigDecimal longitude;

    public void setPostUsername(String username){
        this.username = username;
    }
}
