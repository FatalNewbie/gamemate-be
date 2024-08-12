package com.example.gamemate.domain.post.dto;

import com.example.gamemate.domain.post.entity.Post;
import com.example.gamemate.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.SimpleTimeZone;

@Schema(description = "게시글 응답 DTO")
@Getter
public class PostResponseDTO {

    @Schema(description = "게시글 id")
    private Long id;

    @Schema(description = "유저 아이디")
    private String username;

    @Schema(description = "유저 닉네임")
    private String nickname;

    @Schema(description = "유저 프로필 이미지")
    private String userProfile;


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

    @Schema(description = "장소")
    private String mateLocation;

    @Schema(description = "위도")
    private BigDecimal latitude;

    @Schema(description = "경도")
    private BigDecimal longitude;

    @Schema(description = "생성시간")
    private LocalDateTime createdDate;

    public void setPostUsername(String username){
        this.username = username;
    }


    public PostResponseDTO(Post post) {
        this.id = post.getId();
        this.status = post.getStatus().toString();
        this.gameTitle = post.getGameTitle();
        this.gameGenre = post.getGameGenre();
        this.username = post.getUser().getUsername();
        this.nickname = post.getNickname();
        this.mateCnt = post.getMateCnt();
        this.mateContent = post.getMateContent();
        this.mateRegionGu = post.getMateRegionGu();
        this.mateRegionSi = post.getMateRegionSi();
        this.mateLocation = post.getMateLocation();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
        this.createdDate = post.getCreatedDate();

    }

    @Builder
    public PostResponseDTO(
            Long id, String username, String nickname, String userProfile,
            String status, String gameTitle,
            String gameGenre, Long mateCnt, String mateContent, Long commentCnt,
            String mateRegionSi, String mateRegionGu, String mateLocation,
            BigDecimal latitude, BigDecimal longitude, LocalDateTime createdDate
    ){
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.userProfile =userProfile;

        this.status = status;
        this.gameTitle = gameTitle;
        this.gameGenre = gameGenre;
        this.mateCnt = mateCnt;
        this.mateContent = mateContent;
        this.commentCnt = commentCnt;

        this.mateRegionSi = mateRegionSi;
        this.mateRegionGu = mateRegionGu;
        this.mateLocation = mateLocation;
        this.latitude = latitude;
        this.longitude = longitude;

        this.createdDate = createdDate;
    }

}
