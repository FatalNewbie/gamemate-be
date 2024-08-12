package com.example.gamemate.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Schema(description = "게시글 수정 요청 DTO")
@Getter
public class PostUpdateDTO {

    @Schema(description = "온/오프 상태값")
    @NotNull
    private String status;

    @Schema(description = "모집 인원 수")
    @NotNull
    private String mateContent;


    @Schema(description = "지역(시)")
    private String mateRegionSi;

    @Schema(description = "지역(구)")
    private String mateRegionGu;

    @Schema(description = "위도")
    private BigDecimal latitude;

    @Schema(description = "경도")
    private BigDecimal longitude;
}