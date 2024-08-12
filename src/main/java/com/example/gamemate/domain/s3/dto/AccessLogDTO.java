package com.example.gamemate.domain.s3.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AccessLogDTO {
    private Long id;
    private String userId;
    private String endpoint;
    private LocalDateTime accessTime;
    private LocalDateTime leaveTime;

    public AccessLogDTO(Long id, String userId, String endpoint, LocalDateTime accessTime, LocalDateTime leaveTime) {
        this.id = id;
        this.userId = userId;
        this.endpoint = endpoint;
        this.accessTime = accessTime;
        this.leaveTime = leaveTime;
    }

}
