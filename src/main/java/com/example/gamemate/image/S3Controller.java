package com.example.gamemate.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/uploadform")
    public String showUploadForm() {
        return "uploadForm"; // 업로드 폼 HTML 페이지
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            String fileUrl = s3Service.saveFile(file);
            model.addAttribute("fileUrl", fileUrl);
            return "uploadForm"; // 업로드 성공 페이지
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "File upload failed");
            return "uploadForm"; // 업로드 폼 페이지
        }
    }

    @GetMapping("/downloadform")
    public String showDownloadForm() {
        return "downloadForm"; // 다운로드 폼 HTML 페이지
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam("filename") String filename) {
        try {
            return s3Service.downloadImage(filename);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File download failed");
        }
    }
}