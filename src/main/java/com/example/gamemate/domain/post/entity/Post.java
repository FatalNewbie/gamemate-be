package com.example.gamemate.domain.post.entity;

import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


//setter 어노테이션 허용 시 어디서는 객체의 변경이 가능하기 때문에 사용 지양하기
//대신 빌더 패턴 사용
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    @Column(nullable = false)
    private OnOffStatus status;

    @Column(length = 100)
    private String gameTitle;

    @Column(length = 50)
    private String gameGenre;

    @Column(length = 20)
    private String nickname;

    @Column
    private Integer mateCnt;

    @Column(length = 500)
    private String mateContent;

    @Column(length = 30)
    private String mateRegionSi;

    @Column(length = 30)
    private String mateRegionGu;

    @Column
    private BigDecimal latitude;

    @Column
    private BigDecimal longitude;

    @Column
    private LocalDateTime deletedDate;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<PostComment> postComments = new ArrayList<>();

    public enum OnOffStatus {
        ON,  // 온라인
        OFF  // 오프라인
    }

    public void updateOnlinePost(Integer mateCnt, String mateContent){
        this.mateCnt = mateCnt;
        this.mateContent = mateContent;
    }

    public void updateOfflinePost(Integer mateCnt, String mateContent,
                                  String mateRegionSi, String mateRegionGu,
                                  BigDecimal latitude, BigDecimal longitude){
        this.mateCnt = mateCnt;
        this.mateContent = mateContent;
        this.mateRegionGu = mateRegionGu;
        this.mateRegionSi = mateRegionSi;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Builder
    public Post(User user, OnOffStatus status, String gameTitle, String gameGenre,
                String nickname, Integer mateCnt, String mateContent,
                String mateRegionSi, String mateRegionGu,
                BigDecimal latitude, BigDecimal longitude) {
        this.user = user;
        this.status = status;
        this.gameTitle = gameTitle;
        this.gameGenre = gameGenre;
        this.nickname = nickname;
        this.mateCnt = mateCnt;
        this.mateContent = mateContent;
        this.mateRegionSi = mateRegionSi;
        this.mateRegionGu = mateRegionGu;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
