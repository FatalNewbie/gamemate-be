package com.example.gamemate.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class PostCommentResponseDTO {

    private String username;

    private String nickname;

    private String content;

    private LocalDateTime createdDate;

    private LocalDate modifiedDate;

    public void setCommentUsername(String username){
        this.username = username;
    }
}
