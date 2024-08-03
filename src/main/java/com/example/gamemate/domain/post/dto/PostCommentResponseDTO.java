package com.example.gamemate.domain.post.dto;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PostCommentResponseDTO {

    String username;

    String nickname;

    String content;

    LocalDateTime createdDate;

    LocalDate modifiedDate;
}
