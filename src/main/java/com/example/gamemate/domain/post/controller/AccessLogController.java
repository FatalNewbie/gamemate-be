package com.example.gamemate.domain.post.controller;

import com.example.gamemate.domain.post.entity.AccessLog;
import com.example.gamemate.domain.post.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

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