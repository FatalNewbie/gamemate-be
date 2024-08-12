package com.example.gamemate.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "게시글 공통 부분 요청 DTO")
@Getter
public abstract class PostDTO {

    @Schema(description = "게시글 제목")
    @NotNull
    @NotEmpty(message = "게임 제목은 필수 입력 항목입니다.")
    private String gameTitle;

    @Schema(description = "온/오프 상태값")
    @JsonProperty("status")
    @NotNull
    @NotEmpty(message = "상태는 필수 입력 항목입니다.")
    private String status;

    @Schema(description = "게임 장르")
    @NotNull
    @NotEmpty(message = "게임 장르는 필수 입력 항목입니다.")
    private String gameGenre;

    @Schema(description = "모집 인원 수")
    @NotNull
    @Min(value = 1, message = "모집 인원은 1명 이상이어야 합니다.")
    private Long mateCnt;

    @Schema(description = "게시글 내용")
    @NotNull
    @NotEmpty(message = "내용은 필수 입력 항목입니다.")
    @Size(max = 500, message = "내용은 500자 이하여야 합니다.")
    @Size(min = 10, message = "내용은 10자 이상이어야 합니다.")
    private String mateContent;

    protected void setStatus(String status) {
        this.status = status;
    }
}