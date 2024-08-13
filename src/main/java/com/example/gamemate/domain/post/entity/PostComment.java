package com.example.gamemate.domain.post.entity;

import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.global.audit.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post_comment")
@Entity
public class PostComment extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private PostComment parentComment;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Schema(description = "유저 프로필 이미지")
    private String userProfile;

    @Column(nullable = false)
    private String content;

    @Column
    private LocalDateTime deletedDate;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<PostComment> reComments;

    @Builder
    public PostComment(
            User user, Post post,
            PostComment parentComment,
            String nickname, String content, String userProfile
    ){
        this.user = user;
        this.post = post;
        this.parentComment = parentComment;
        this.nickname = nickname;
        this.userProfile = userProfile;
        this.content = content;
    }

    public void updateComment(String content){
        this.content = content;
    }

    public void deleteComment(LocalDateTime deletedDate, String content){
        this.deletedDate = deletedDate;
        this.content = content;
    }

}