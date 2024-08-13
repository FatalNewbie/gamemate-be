package com.example.gamemate.domain.post.repository;

import com.example.gamemate.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository{

    Page<Post> findByStatusOrderByCreatedDateDesc(Post.OnOffStatus status, Pageable pageable);

    Page<Post> findByUserId(Long userId, Pageable pageable);

}