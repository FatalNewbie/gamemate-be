package com.example.gamemate.domain.s3.controller;

import com.example.gamemate.domain.s3.entity.AccessLog;
import com.example.gamemate.domain.s3.service.AccessLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AccessLogController {


    private final AccessLogService accessLogService;

    public AccessLogController(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @GetMapping("/logs")
    public String getAllLogs(Model model) {
        List<AccessLog> logs = accessLogService.getAllLogs();
        model.addAttribute("logs", logs);
        return "logs";
    }

    @GetMapping("/averageTime")
    public ResponseEntity<Double> getAverageTimeOnPage() {
        double averageTime = accessLogService.getOverallAverageTime();
        return ResponseEntity.ok(averageTime); // 직접 double 값을 반환
    }


}