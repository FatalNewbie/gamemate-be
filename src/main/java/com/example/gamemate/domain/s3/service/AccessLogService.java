package com.example.gamemate.domain.s3.service;


import com.example.gamemate.domain.s3.dto.AccessLogDTO;
import com.example.gamemate.domain.s3.entity.AccessLog;
import com.example.gamemate.domain.s3.repository.AccessLogRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccessLogService {


    private final AccessLogRepository accessLogRepository;

    public AccessLogService(AccessLogRepository accessLogRepository){
        this.accessLogRepository = accessLogRepository;
    }

    public void saveAccessLog(AccessLogDTO accessLogDTO) {

        AccessLog accessLog = new AccessLog(
                accessLogDTO.getUserName(),
                accessLogDTO.getEndpoint(),
                accessLogDTO.getAccessTime(),
                accessLogDTO.getLeaveTime()
        );
        accessLogRepository.save(accessLog);
    }
}
