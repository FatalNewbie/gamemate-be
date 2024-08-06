package com.example.gamemate.domain.s3.service;

import com.example.gamemate.domain.auth.jwt.JWTUtil;
import com.example.gamemate.domain.s3.entity.AccessLog;
import com.example.gamemate.domain.s3.repository.AccessLogRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;



@Service
public class AccessLogService {


    private final AccessLogRepository accessLogRepository;

    private final JWTUtil JWTUtil;


    public AccessLogService(AccessLogRepository accessLogRepository, com.example.gamemate.domain.auth.jwt.JWTUtil JWTUtil){
        this.accessLogRepository = accessLogRepository;
        this.JWTUtil = JWTUtil;
    }

    public AccessLog logAccess( String endpoint, String token) {
        AccessLog log = new AccessLog();
        log.setUserId(JWTUtil.getUsername(token));
        log.setEndpoint(endpoint);
        log.setAccessTime(LocalDateTime.now());
        return accessLogRepository.save(log);
    }

    public void logLeave(Long logId) {
        AccessLog log = accessLogRepository.findById(logId).orElse(null);
        if (log != null) {
            log.setLeaveTime(LocalDateTime.now());
            accessLogRepository.save(log);
        }
    }

    public double getOverallAverageTime() {
        List<AccessLog> logs = accessLogRepository.findAll(); // 모든 로그를 가져옵니다.

        // 머무름 시간이 기록되지 않은 로그 제외
        logs.removeIf(log -> log.getLeaveTime() == null || log.getAccessTime() == null);

        // 총 머무름 시간 계산 (초 단위)
        long totalDuration = logs.stream()
                .mapToLong(log -> {
                    LocalDateTime accessTime = log.getAccessTime();
                    LocalDateTime leaveTime = log.getLeaveTime();

                    // AccessTime이 LeaveTime보다 나중인 경우
                    if (leaveTime.isBefore(accessTime)) {
                        System.out.println("Invalid log: Access Time is after Leave Time for log ID: " + log.getId());
                        return 0; // 무시
                    }

                    // Duration 계산 후 초 단위로 반환
                    return Duration.between(accessTime, leaveTime).getSeconds();
                })
                .sum();

        // 평균 머무름 시간 계산
        return logs.isEmpty() ? 0 : (double) totalDuration / logs.size();
    }

    public List<AccessLog> getAllLogs() {
        return accessLogRepository.findAll();
    }
}
