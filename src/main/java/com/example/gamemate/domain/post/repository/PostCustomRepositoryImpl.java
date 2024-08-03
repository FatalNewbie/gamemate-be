package com.example.gamemate.domain.post.repository;

import com.example.gamemate.domain.post.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.example.gamemate.domain.post.entity.QPost.post;
import static com.example.gamemate.domain.post.entity.QPostComment.postComment;


@Repository
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PostCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public void hardDeleteComments(Long id) {

        //

        jpaQueryFactory
                .delete(postComment)
                .where(postComment.post.id.eq(id)
                        .and(postComment.deletedDate.isNotNull()))
                .execute();
    }
}
