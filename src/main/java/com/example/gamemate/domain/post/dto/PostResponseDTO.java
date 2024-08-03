package com.example.gamemate.domain.post.dto;

import com.example.gamemate.domain.post.entity.Post;
import com.example.gamemate.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.SimpleTimeZone;

@Getter
public class PostResponseDTO {

    private String status;
    private String gameTitle;
    private String gameGenre;
    private String username;
    private String nickname;
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
        this.username = post.getUser().getUsername();
        this.nickname = post.getNickname();
        this.mateCnt = post.getMateCnt();
        this.mateContent = post.getMateContent();
        this.mateRegionGu = post.getMateRegionGu();
        this.mateRegionSi = post.getMateRegionSi();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();

    }

    @Builder
    public PostResponseDTO(String status, String gameTitle, String gameGenre, String username, String nickname,Integer mateCnt, String mateContent,
                           String mateRegionSi, String mateRegionGu, BigDecimal latitude, BigDecimal longitude, List<PostCommentsResponseDTO> postComments){
        this.status = status;
        this.gameTitle = gameTitle;
        this.gameGenre = gameGenre;
        this.username = username;
        this.nickname = nickname;
        this.mateCnt = mateCnt;
        this.mateContent = mateContent;
        this.mateRegionSi = mateRegionSi;
        this.mateRegionGu = mateRegionGu;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postComments = postComments; // 필요시 초기화 로직 추가
    }

}
