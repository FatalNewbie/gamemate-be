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

@Controller
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;
    private final AccessLogService accessLogService;




}