package com.example.gamemate.domain.s3.controller;

import com.example.gamemate.domain.s3.dto.AccessLogDTO;
import com.example.gamemate.domain.s3.entity.AccessLog;
import com.example.gamemate.domain.s3.service.AccessLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class AccessLogController {


    private final AccessLogService accessLogService;

    public AccessLogController(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @PostMapping("/access")
    public ResponseEntity<Void> logAccess(@RequestBody AccessLogDTO accessLogDTO) {
        accessLogService.saveAccessLog(accessLogDTO);
        return ResponseEntity.ok().build();
    }

}