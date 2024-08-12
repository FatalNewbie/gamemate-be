package com.example.gamemate.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "오프라인 게시글 요청 DTO")
@Getter
@Setter
public class OfflinePostDTO extends PostDTO {

    public OfflinePostDTO() {
        super.setStatus("OFF");
    }

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

}