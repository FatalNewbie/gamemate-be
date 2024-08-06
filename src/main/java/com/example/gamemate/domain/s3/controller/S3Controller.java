package com.example.gamemate.domain.s3.controller;

import com.example.gamemate.domain.s3.service.AccessLogService;
import com.example.gamemate.domain.s3.service.S3Service;
import com.example.gamemate.domain.s3.entity.AccessLog;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;
    private final AccessLogService accessLogService;


    @GetMapping("/uploadform")
    public String showUploadForm(@RequestParam String token) {
        AccessLog log = accessLogService.logAccess("/uploadform", token);

        return "uploadForm";
    }

    @PostMapping("/leave")
    public void logLeave(@RequestBody Long logId) {
        accessLogService.logLeave(logId);
    }



    @GetMapping("/presigned-url")
    public ResponseEntity<String> getPresignedUrl(@RequestParam String bucketName, @RequestParam String objectKey) {
        try {
            URL presignedUrl = s3Service.generatePresignedUrl(bucketName, objectKey);
            return new ResponseEntity<>(presignedUrl.toString(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to generate presigned URL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/images")
    public ResponseEntity<List<String>> getImages(@RequestParam(value = "continuationToken", required = false) String continuationToken) {
        List<String> images = s3Service.listFiles(continuationToken);
        return ResponseEntity.ok(images);
    }
}