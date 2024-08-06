package com.example.gamemate.domain.s3.controller;

import com.example.gamemate.domain.s3.dto.AccessLogDTO;
import com.example.gamemate.domain.s3.entity.AccessLog;
import com.example.gamemate.domain.s3.service.AccessLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AccessLogController {


    private final AccessLogService accessLogService;

    public AccessLogController(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    //여기서 할 필요 없음.
    @GetMapping("/logs")
    public ResponseEntity<List<AccessLogDTO>> getAllLogs() {
        List<AccessLog> logs = accessLogService.getAllLogs();
        List<AccessLogDTO> logDTOs = logs.stream()
                .map(log -> new AccessLogDTO(log.getId(), log.getUserId(), log.getEndpoint(),
                        log.getAccessTime(), log.getLeaveTime()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(logDTOs, HttpStatus.OK);
    }

    @GetMapping("/averageTime")
    public ResponseEntity<Double> getAverageTimeOnPage() {
        double averageTime = accessLogService.getOverallAverageTime();
        return ResponseEntity.ok(averageTime); // 직접 double 값을 반환
    }


}