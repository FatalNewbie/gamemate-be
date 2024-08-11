package com.example.gamemate.domain.post.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OfflinePostDTO extends PostDTO {

    public OfflinePostDTO() {
        super.setStatus("OFF");
    }

    private String mateRegionSi;

    private String mateRegionGu;

    private String mateLocation;

    private BigDecimal latitude;

    private BigDecimal longitude;

}