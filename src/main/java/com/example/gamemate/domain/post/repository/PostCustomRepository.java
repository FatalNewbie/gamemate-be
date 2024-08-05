package com.example.gamemate.domain.post.repository;


import com.example.gamemate.domain.post.entity.Post;

import java.util.List;

public interface PostCustomRepository {

    void hardDeleteComments(Long id);

    void setCommentsToNull(Long id);

    Long countCommentsByPostId(Long postId);

}