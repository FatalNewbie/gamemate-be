package com.example.gamemate.domain.post.repository;

import com.example.gamemate.domain.post.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long>, PostCommentCustomRepository {
    List<PostComment> findRepliesByParentCommentId(Long parentCommentId);

    // Post ID로 댓글을 가져오고, parentComment가 null인 경우만 필터링
    List<PostComment> findByidAndParentCommentIsNull(Long id);

}
