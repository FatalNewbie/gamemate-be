package com.example.gamemate.domain.s3.controller;

import com.example.gamemate.domain.s3.service.AccessLogService;
import com.example.gamemate.domain.s3.service.S3Service;
import com.example.gamemate.domain.s3.entity.AccessLog;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping("/presigned-url")
    public ResponseEntity<String> getPresignedUrl(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        URL presignedUrl = s3Service.generatePresignedUrl(username);
        return ResponseEntity.ok(presignedUrl.toString());
    }

    @GetMapping("s3/images")
    public ResponseEntity<List<String>> getImages() {
        List<String> imageUrls = s3Service.listImages();
        return ResponseEntity.ok(imageUrls); // 이미지 URL 리스트 반환
    }



}