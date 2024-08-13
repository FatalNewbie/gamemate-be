package com.example.gamemate.domain.post.repository;

import com.example.gamemate.domain.post.entity.PostComment;
import com.example.gamemate.domain.post.entity.QPostComment;
import com.example.gamemate.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.gamemate.domain.post.entity.QPostComment.postComment;
import static com.example.gamemate.domain.user.entity.QUser.user;

public class PostCommentCustomRepositoryImpl implements PostCommentCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public PostCommentCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<PostComment> findPostCommentsById(Long id) {
        return jpaQueryFactory.selectFrom(postComment)
                .where(postComment.post.id.eq(id)
                        .and(postComment.parentComment.isNull()))
                .fetch();
    }

    @Override
    public boolean isRecomment(Long commentId) {

        Boolean exists = jpaQueryFactory
                .select(postComment.parentComment.isNotNull())
                .from(postComment)
                .where(postComment.id.eq(commentId))
                .fetchOne();

        return exists != null && exists; //commentId가 존재하지 않는 경우도 고려
    }

    @Override
    public Long hasRecomments(Long commentId) {

        Long count = jpaQueryFactory
                .select(postComment.id.count())
                .from(postComment)
                .where(postComment.parentComment.id.eq(commentId))
                .fetchOne();

        return count; //대댓글이 존재하면 true, 댓글만 있으면 false
    }


    //부모 댓글이 삭제되었는지 확인
    @Override
    public boolean isParentCommentDeleted(Long commentId) {

        //부모 댓글의 deletedDate가 null인지 확인
        if(commentId != null){

            LocalDateTime deletedDate = jpaQueryFactory
                    .select(postComment.deletedDate)
                    .from(postComment)
                    .where(postComment.id.eq(commentId))
                    .fetchOne();

            return deletedDate != null;
        }
        return false;
    }

    //대댓글의 부모 id 반환
    @Override
    public Long getPCommentId(Long recommentId) {

        return jpaQueryFactory
                .select(postComment.parentComment.id)
                .from(postComment)
                .where(postComment.id.eq(recommentId))
                .fetchOne();
    }

    @Override
    public String findUsernameByCommentId(Long commentId) {

        return jpaQueryFactory
                .select(user.username)
                .from(postComment)
                .join(postComment.user, user)
                .where(postComment.id.eq(commentId))
                .fetchOne();
    }

    @Override
    public String findNicknameByCommentId(Long commentId) {
        return jpaQueryFactory
                .select(user.nickname)
                .from(postComment)
                .join(postComment.user, user)
                .where(postComment.id.eq(commentId))
                .fetchOne();
    }

}