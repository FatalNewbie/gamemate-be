package com.example.gamemate.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "status") // status 필드를 타입 구분자로 사용
@JsonSubTypes({
        @JsonSubTypes.Type(value = OnlinePostDTO.class, name = "ON"),
        @JsonSubTypes.Type(value = OfflinePostDTO.class, name = "OFF")
})
@Getter
public abstract class PostDTO {

    @NotNull
    private String gameTitle;

    @JsonProperty("status")
    @NotNull
    private String status;

    @NotNull
    private String gameGenre;

    @NotNull
    @Min(value = 1, message = "모집 인원은 1명 이상이어야 합니다.")
    private Integer mateCnt;

    @NotNull
    @Size(max = 500, message = "내용은 500자 이하여야 합니다.")
    private String mateContent;

    protected void setStatus(String status) {
        this.status = status;
    }
}