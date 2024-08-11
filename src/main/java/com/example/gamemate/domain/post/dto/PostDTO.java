package com.example.gamemate.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "게임 제목은 필수 입력 항목입니다.")
    private String gameTitle;

    @JsonProperty("status")
    @NotNull
    @NotEmpty(message = "상태는 필수 입력 항목입니다.")
    private String status;

    @NotNull
    @NotEmpty(message = "게임 장르는 필수 입력 항목입니다.")
    private String gameGenre;

    @NotNull
    @Min(value = 1, message = "모집 인원은 1명 이상이어야 합니다.")
    private Long mateCnt;

    @NotNull
    @NotEmpty(message = "내용은 필수 입력 항목입니다.")
    @Size(max = 500, message = "내용은 500자 이하여야 합니다.")
    private String mateContent;

    protected void setStatus(String status) {
        this.status = status;
    }
}