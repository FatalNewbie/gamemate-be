package com.example.gamemate.domain.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.HttpMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.net.URL;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;


    public URL generatePresignedUrl(String bucketName, String objectKey) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1시간
        expiration.setTime(expTimeMillis);

        // Presigned URL 요청 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    }

    //s3 에서 가져올때 페이지네이션이 되는가?
    public List<String> listFiles(String continuationToken) {
        ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withMaxKeys(10) // 한 번에 가져올 객체 수
                .withContinuationToken(continuationToken);

        ListObjectsV2Result result = amazonS3.listObjectsV2(listObjectsRequest);

        return result.getObjectSummaries().stream()
                .map(summary -> summary.getKey())
                .collect(Collectors.toList());
    }


    public String getNextContinuationToken() {
        ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withMaxKeys(1); // 첫 페이지 요청

        ListObjectsV2Result result = amazonS3.listObjectsV2(listObjectsRequest);

        return result.getNextContinuationToken();
    }


}