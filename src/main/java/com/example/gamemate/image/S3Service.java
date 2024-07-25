package com.example.gamemate.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucketName, uniqueFileName, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucketName, uniqueFileName).toString();
    }

    public ResponseEntity<UrlResource> downloadImage(String originalFilename) throws MalformedURLException {
        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucketName, originalFilename).toString());

        String contentDisposition = "attachment; filename=\"" + originalFilename + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }
}