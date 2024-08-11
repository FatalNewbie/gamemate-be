package com.example.gamemate.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class PostUpdateResponseDTO {

    private String username;

    private String nickname;


    private String status;

    private String gameTitle;

    private String gameGenre;

    private Long mateCnt;

    private String mateContent;

    private Long commentCnt;

    private String mateRegionSi;

    private String mateRegionGu;

    private BigDecimal latitude;

    private BigDecimal longitude;

    public void setPostUsername(String username){
        this.username = username;
    }
}
