package com.example.gamemate.domain.post.repository;

import com.example.gamemate.domain.post.entity.Post;
import com.example.gamemate.domain.post.entity.PostComment;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.beans.Expression;
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

        //해당 게시글에서 모든 댓글 리스트를 가져옴
        List<PostComment> allCommentsToDelete = jpaQueryFactory
                        .selectFrom(postComment)
                        .where(postComment.post.id.eq(id))
                                .fetch();

        //삭제된 댓글 리스트를 돌면서 post_id를 null로 변경
        if(!allCommentsToDelete.isEmpty()){
            for(PostComment comment : allCommentsToDelete){

                // 댓글의 외래 키를 null로 변경
                jpaQueryFactory
                        .update(postComment)
                        .set(postComment.post.id, Expressions.nullExpression()) // parentId를 null로 설정 // 삭제할 댓글 ID
                        .execute();

                jpaQueryFactory
                        .delete(postComment)
                        .where(postComment.id.eq(comment.getId()))
                        .execute();
            }
        }

        List<PostComment> commentsToDelete = jpaQueryFactory
                .selectFrom(postComment)
                .where(postComment.post.id.eq(id)
                        .and(postComment.deletedDate.isNotNull()))
                .fetch();

        //삭제된 댓글 리스트를 돌면서 pCommentId를 null로 변경
        if(!commentsToDelete.isEmpty()){
            for(PostComment comment : commentsToDelete){

                // 댓글의 외래 키를 null로 변경
                jpaQueryFactory
                        .update(postComment)
                        .set(postComment.parentComment.id, Expressions.nullExpression()) // parentId를 null로 설정 // 삭제할 댓글 ID
                        .execute();

                jpaQueryFactory
                        .delete(postComment)
                        .where(postComment.id.eq(comment.getId()))
                        .execute();
            }
        }
    }

    // 특정 ID의 게시글을 삭제하기 전에 자식 댓글의 post 필드를 NULL로 설정
    public void setCommentsToNull(Long postId) {
        // 자식 댓글의 'post' 필드를 NULL로 설정하는 업데이트 쿼리
        long affectedRows = jpaQueryFactory
                .update(postComment)
                .set(postComment.post, Expressions.nullExpression())
                .where(postComment.post.id.eq(postId))
                .execute();
    }

    @Override
    public Long countCommentsByPostId(Long postId) {
        return jpaQueryFactory
                .select(postComment.count())
                .from(postComment)
                .where(postComment.post.id.eq(postId)
                        .and(postComment.deletedDate.isNull()))
                .fetchOne();
    }
}
