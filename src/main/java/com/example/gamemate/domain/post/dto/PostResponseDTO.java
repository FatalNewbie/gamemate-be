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

    private Long id;

    private String username;

    private String nickname;


    private String status;

    private String gameTitle;

    private String gameGenre;

    private Integer mateCnt;

    private String mateContent;

    private Long commentCnt;



    private String mateRegionSi;

    private String mateRegionGu;

    private BigDecimal latitude;

    private BigDecimal longitude;



    private List<PostCommentsResponseDTO> postComments;

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
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();

    }

    @Builder
    public PostResponseDTO(
            Long id, String username, String nickname, String status, String gameTitle,
            String gameGenre, Integer mateCnt, String mateContent, Long commentCnt,
            String mateRegionSi, String mateRegionGu, BigDecimal latitude, BigDecimal longitude,
            List<PostCommentsResponseDTO> postComments
    ){
        this.id = id;
        this.username = username;
        this.nickname = nickname;

        this.status = status;
        this.gameTitle = gameTitle;
        this.gameGenre = gameGenre;
        this.mateCnt = mateCnt;
        this.mateContent = mateContent;
        this.commentCnt = commentCnt;

        this.mateRegionSi = mateRegionSi;
        this.mateRegionGu = mateRegionGu;
        this.latitude = latitude;
        this.longitude = longitude;

        this.postComments = postComments; // 필요시 초기화 로직 추가
    }

}
