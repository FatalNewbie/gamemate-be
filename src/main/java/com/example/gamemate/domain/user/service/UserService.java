package com.example.gamemate.domain.user.service;




import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.gamemate.domain.user.dto.MyPageResponseDTO;
import com.example.gamemate.domain.user.dto.UpdateDTO;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.mapper.UserMapper;
import com.example.gamemate.domain.user.repository.UserRepository;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.web.multipart.MultipartFile;


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

    public void uploadProfileImage(String username, MultipartFile file) throws IOException {
        String objectKey = username + "_profile_image";
        s3Client.putObject(new PutObjectRequest(bucketName, objectKey, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }


}
