package com.example.gamemate.domain.user.service;




import com.amazonaws.services.s3.model.*;
import com.example.gamemate.domain.user.dto.MyPageResponseDTO;
import com.example.gamemate.domain.user.dto.UpdateDTO;
import com.example.gamemate.domain.user.dto.RecommendResponseDTO;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.mapper.UserMapper;
import com.example.gamemate.domain.user.repository.UserRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.HttpMethod;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper mapper;
    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;



    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserMapper mapper, AmazonS3 s3Client){

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mapper = mapper;
        this.s3Client = s3Client;

    }

    public MyPageResponseDTO findByUsernameForMyPage(String username) {

        User user = userRepository.findByUsername(username);

        if(!user.equals("")) {
            return mapper.userToMyPageDto(user);
        }

        return null;

    }

    public RecommendResponseDTO findByUsernameForRecommendation(String username) {
        User user = userRepository.findByUsername(username);

        if(!user.equals("")) {
            return mapper.userToRecommendResponseDTO(user);
        }
        return null;
    }

    public UpdateDTO findByUsernameForUpdate(String username){

        User user = userRepository.findByUsername(username);
        if(!user.equals("")){
            return mapper.userToUpdateDto(user);
        }

        return null;

    }

    public UpdateDTO update(UpdateDTO updateDTO) {
        // 사용자 엔티티 생성
        User user = mapper.updateDtoToUser(updateDTO);

        // 비밀번호 암호화
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        // 사용자 정보 업데이트
        User updatedUser = userRepository.save(user);

        // 업데이트된 사용자 DTO 반환
        return mapper.userToUpdateDto(updatedUser);
    }

    public void deletedByUsername(String username) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));
        log.info("finduser: " + String.valueOf(optionalUser));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setDeleted(true);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public void restorationByUsername(String username) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setDeleted(false);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public URL generatePresignedUrl(String username) {
        String objectKey = username + "_profile_image";

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1시간
        expiration.setTime(expTimeMillis);

        // presigned URL 요청 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);

        return s3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }


    public String getProfileImageUrl(String username) {
        String objectKey = username + "_profile_image";

        // S3에서 프로필 이미지가 있는지 확인
        if (s3Client.doesObjectExist(bucketName, objectKey)) {
            return s3Client.getUrl(bucketName, objectKey).toString();
        } else {
            // 기본 이미지 URL 반환
            return "https://example.com/default-profile-image.png"; // 기본 이미지 URL
        }
    }


    public String uploadProfileImage(String username, MultipartFile file) throws IOException {
        String objectKey = username + "_profile_image";

        // S3에 파일 업로드
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        s3Client.putObject(new PutObjectRequest(bucketName, objectKey, file.getInputStream(), metadata));

        // 업로드된 이미지 URL 생성
        return s3Client.getUrl(bucketName, objectKey).toString(); // 이미지 URL 반환
    }

    public void updateUserProfileImage(String username, String imageUrl) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setUserProfile(imageUrl); // userProfile 필드에 이미지 URL 저장
            userRepository.save(user); // 변경사항 저장
            System.out.println("프로필 이미지 업데이트 완료: " + imageUrl);
        } else {
            System.out.println("사용자를 찾을 수 없습니다: " + username);
        }
    }
    public List<String> listImages() {
        List<String> imageUrls = new ArrayList<>();

        ObjectListing objectListing = s3Client.listObjects(bucketName);
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            String imageUrl = s3Client.getUrl(bucketName, objectSummary.getKey()).toString();
            imageUrls.add(imageUrl); // 이미지 URL 리스트에 추가
        }

        return imageUrls; // 이미지 URL 리스트 반환
    }


}
