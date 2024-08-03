package com.example.gamemate.domain.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //private String userId;

    private String endpoint;

    private LocalDateTime accessTime;

    private LocalDateTime leaveTime;

    // 로그 설명 추가(페이지 접속, 기능 실행 등)
    public AccessLog() {}


    //userId 나중에 추가하기
    public AccessLog(String endpoint, LocalDateTime accessTime) {

        //this.userId = userId;
        this.endpoint = endpoint;
        this.accessTime = accessTime;

    }


}