package com.example.gamemate.domain.post.mapper;

import com.example.gamemate.domain.post.dto.PostCommentResponseDTO;
import com.example.gamemate.domain.post.entity.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostCommentMapper {

    PostCommentResponseDTO postCommentToPostCommentResponseDTO(PostComment postComment);
}
