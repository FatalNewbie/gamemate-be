package com.example.gamemate.domain.s3.service;

import com.example.gamemate.domain.auth.jwt.JWTUtil;

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

    private final JWTUtil JWTUtil;


    public AccessLogService(AccessLogRepository accessLogRepository, com.example.gamemate.domain.auth.jwt.JWTUtil JWTUtil){
        this.accessLogRepository = accessLogRepository;
        this.JWTUtil = JWTUtil;
    }


}
