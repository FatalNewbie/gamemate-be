package com.example.gamemate.domain.post.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PostUpdateDTO {

    @NotNull
    private String status;

    @NotNull
    private Long mateCnt;

    @NotNull
    private String mateContent;


    private String mateRegionSi;

    private String mateRegionGu;

    private BigDecimal latitude;

    private BigDecimal longitude;
}