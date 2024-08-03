package com.example.gamemate.domain.post.dto;

import com.example.gamemate.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class PostResponseDTO {

    private String status;
    private String gameTitle;
    private String gameGenre;
    private Long userId;
    private Integer mateCnt;
    private String mateContent;
    private String mateRegionSi;
    private String mateRegionGu;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<PostCommentsResponseDTO> postComments;


    public PostResponseDTO(Post post) {
        this.status = post.getStatus().toString();
        this.gameTitle = post.getGameTitle();
        this.gameGenre = post.getGameGenre();
        this.userId = post.getUserId();
        this.mateCnt = post.getMateCnt();
        this.mateContent = post.getMateContent();
        this.mateRegionGu = post.getMateRegionGu();
        this.mateRegionSi = post.getMateRegionSi();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();

    }

    @Builder
    public PostResponseDTO(String status, String gameTitle, String gameGenre, Long userId, Integer mateCnt, String mateContent,
                           String mateRegionSi, String mateRegionGu, BigDecimal latitude, BigDecimal longitude, List<PostCommentsResponseDTO> postComments){
        this.status = status;
        this.gameTitle = gameTitle;
        this.gameGenre = gameGenre;
        this.userId = userId;
        this.mateCnt = mateCnt;
        this.mateContent = mateContent;
        this.mateRegionSi = mateRegionSi;
        this.mateRegionGu = mateRegionGu;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postComments = postComments; // 필요시 초기화 로직 추가
    }

}
