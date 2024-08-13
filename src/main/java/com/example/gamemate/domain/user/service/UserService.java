package com.example.gamemate.domain.user.service;




import com.amazonaws.services.s3.model.*;
import com.example.gamemate.domain.user.dto.MyPageResponseDTO;
import com.example.gamemate.domain.user.dto.UpdateDTO;
import com.example.gamemate.domain.user.dto.RecommendResponseDTO;
import com.example.gamemate.domain.user.entity.Genre;
import com.example.gamemate.domain.user.entity.PlayTime;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.mapper.CustomUserMapper;
import com.example.gamemate.domain.user.mapper.UserMapper;
import com.example.gamemate.domain.user.repository.GenreRepository;
import com.example.gamemate.domain.user.repository.PlayTimeRepository;
import com.example.gamemate.domain.user.repository.UserRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final GenreRepository genreRepository;
    private final PlayTimeRepository playTimeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper mapper;
    private final CustomUserMapper customUserMapper;
    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;



    public UserService(UserRepository userRepository,
                       GenreRepository genreRepository,
                       PlayTimeRepository playTimeRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserMapper mapper,
                       AmazonS3 s3Client,
                       CustomUserMapper customUserMapper){

        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
        this.playTimeRepository = playTimeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mapper = mapper;
        this.s3Client = s3Client;
        this.customUserMapper = customUserMapper;

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
            return mapper.userToUpdateDto(user, customUserMapper);
        }

        return null;

    }

    public UpdateDTO update(UpdateDTO updateDTO) {
        User existingUser = userRepository.findByUsername(updateDTO.getUsername());

        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        // 사용자 엔티티 업데이트
        User user = customUserMapper.updateDtoToUser(updateDTO, existingUser);

        // 비밀번호 암호화
        if (updateDTO.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }

        // 선호 장르 업데이트
        if (updateDTO.getPreferredGenres() != null) {
            List<Genre> genres = genreRepository.findAllById(
                    updateDTO.getPreferredGenres().stream()
                            .map(Long::valueOf)
                            .collect(Collectors.toList())
            );
            user.setPreferredGenres(genres);
        }

        // 플레이 시간대 업데이트
        if (updateDTO.getPlayTimes() != null) {
            List<PlayTime> playTimes = playTimeRepository.findAllById(
                    updateDTO.getPlayTimes().stream()
                            .map(Long::valueOf)
                            .collect(Collectors.toList())
            );
            user.setPlayTimes(playTimes);
        }

        // 사용자 정보 업데이트
        User updatedUser = userRepository.save(user);

        // 업데이트된 사용자 DTO 반환
        return customUserMapper.userToUpdateDto(updatedUser);
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



}
