package com.example.gamemate.domain.s3.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AccessLogDTO {
    private String userName;

    private String endpoint;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime accessTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime leaveTime;

    public void setAccessTime(String accessTime) {
        this.accessTime = OffsetDateTime.parse(accessTime).toLocalDateTime();
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = OffsetDateTime.parse(leaveTime).toLocalDateTime();
    }
}
