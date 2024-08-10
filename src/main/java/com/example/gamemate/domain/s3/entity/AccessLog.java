package com.example.gamemate.domain.s3.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "이메일 형식이 유효하지 않습니다.")
    private String userName;

    private String endpoint;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime accessTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime leaveTime;

    public AccessLog(String userName, String endpoint, LocalDateTime accessTime, LocalDateTime leaveTime) {
        this.userName = userName;
        this.endpoint = endpoint;
        this.accessTime = accessTime;
        this.leaveTime = leaveTime;
    }


}