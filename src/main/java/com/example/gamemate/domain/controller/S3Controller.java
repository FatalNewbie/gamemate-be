package com.example.gamemate.domain.controller;

import com.example.gamemate.domain.service.AccessLogService;
import com.example.gamemate.domain.service.S3Service;
import com.example.gamemate.domain.entity.AccessLog;

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

@Controller
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;
    private final AccessLogService accessLogService;

    // 페이지 accesstime, leavetime 추가할 예정
    @GetMapping("/uploadform")
    public String showUploadForm() {
        AccessLog log = accessLogService.logAccess("/uploadform");
        RequestContextHolder.currentRequestAttributes().setAttribute("logId", log.getId(),
                RequestAttributes.SCOPE_SESSION);
        return "uploadForm";
    }

    // 서버 시간은 의미 x. 프론트에서
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model, SessionStatus sessionStatus) {
        Long logId = (Long) model.getAttribute("logId");
        AccessLog log = accessLogService.logAccess("/upload"); // 로그 기록
        model.addAttribute("logId", log.getId()); // 로그 ID를 세션에 저장

        try {
            String fileUrl = s3Service.saveFile(file);
            model.addAttribute("fileUrl", fileUrl);
            accessLogService.logLeave(log.getId()); // 로그 종료
            sessionStatus.setComplete();
            return "redirect:/averageTime.html";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "File upload failed");
            accessLogService.logLeave(log.getId()); // 로그 종료
            sessionStatus.setComplete();
            return "redirect:/averageTime.html";
        }
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
}