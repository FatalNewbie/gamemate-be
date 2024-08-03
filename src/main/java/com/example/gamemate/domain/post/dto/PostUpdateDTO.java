package com.example.gamemate.domain.post.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PostUpdateDTO {

    @NotNull
    String status;

    @NotNull
    Integer mateCnt;

    @NotNull
    String mateContent;

    String mateRegionSi;
    String mateResionGu;
    BigDecimal latitude;
    BigDecimal longitude;

}
