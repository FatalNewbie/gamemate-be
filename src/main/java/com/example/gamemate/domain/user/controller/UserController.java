package com.example.gamemate.domain.user.controller;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.gamemate.domain.auth.dto.CustomUserDetailsDTO;
import com.example.gamemate.domain.user.dto.MyPageResponseDTO;
import com.example.gamemate.domain.user.dto.RecommendResponseDTO;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.dto.UpdateDTO;
import com.example.gamemate.domain.user.mapper.UserMapper;
import com.example.gamemate.domain.user.service.UserService;
import com.example.gamemate.global.apiRes.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Slf4j
@Controller
@ResponseBody
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    public UserController(UserService userService, UserMapper mapper) {

        this.userService = userService;
        this.mapper = mapper;

    }

    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponseDTO> mypage(@AuthenticationPrincipal CustomUserDetailsDTO customUserDetailsDTO) {
        String name = customUserDetailsDTO.getUsername();
        String role = customUserDetailsDTO.getAuthorities().iterator().next().getAuthority();

        // 역할 확인 및 로깅
        log.info(role);
        log.info("Accessing mypage");

        MyPageResponseDTO myPageDto = userService.findByUsernameForMyPage(name);

        return ResponseEntity.ok().header("Content-Type", "application/json").body(myPageDto);
    }

    @GetMapping("/info")
    public ApiResponse<RecommendResponseDTO> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        RecommendResponseDTO user = userService.findByUsernameForRecommendation(username);
        if (user == null) {
            return ApiResponse.failureRes(HttpStatus.NOT_FOUND, "User not found", null);
        }
        return ApiResponse.successRes(HttpStatus.OK, user);
    }
    @PutMapping("/update")
    public String updateUser(@RequestBody UpdateDTO updateDTO, @AuthenticationPrincipal UserDetails userDetails) {
        String name = userDetails.getUsername();

        UpdateDTO user = userService.findByUsernameForUpdate(name);

        // 입력받은 정보로 사용자 정보 업데이트
        user.setPassword(updateDTO.getPassword());
        user.setNickname(updateDTO.getNickname());

        // 사용자 정보 업데이트
        UpdateDTO updatedUser = userService.update(user);

        return "수정완료";
    }

    @GetMapping("/delete")
    public String deleteByName(@RequestParam("username") String username) {
        // 로그 추가
        log.info("deleteCheck");

        userService.deletedByUsername(username);
        return "삭제완료";
    }


    @PutMapping("/restoreUser/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public String restoreUser(@PathVariable String username) {
        userService.restorationByUsername(username); // userService에서 회원 복구 작업을 처리
        return "복구완료"; // 복구 성공 메시지 반환
    }

    @GetMapping("/presigned-url")
    public ResponseEntity<String> getPresignedUrl(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        URL presignedUrl = userService.generatePresignedUrl(username);
        return ResponseEntity.ok(presignedUrl.toString());
    }
    @GetMapping("/profile")
    public ResponseEntity<String> getProfileImage(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        String imageUrl = userService.getProfileImageUrl(username);
        return ResponseEntity.ok(imageUrl);
    }

    @PostMapping("/profile/update")
    public ResponseEntity<Void> updateProfileImage(@RequestBody UpdateDTO updateDTO) {
        try {
            // 사용자 이름과 이미지 URL을 사용하여 프로필 업데이트
           userService.updateUserProfileImage(updateDTO.getUsername(), updateDTO.getUserProfile());
            return ResponseEntity.ok().build(); // 성공적으로 업데이트
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // 오류 발생 시 500 반환
        }
    }

    @GetMapping("s3/images")
    public ResponseEntity<List<String>> getImages() {
        List<String> imageUrls = userService.listImages();
        return ResponseEntity.ok(imageUrls); // 이미지 URL 리스트 반환
    }

    // 겟 요청시 닉네임 반환, 이삭 추가
    @GetMapping("/user")
    public ResponseEntity<String> getUserNickname(@AuthenticationPrincipal UserDetails userDetails){
        String nickname = userService.findByUsernameForMyPage(userDetails.getUsername()).getNickname();
        return ResponseEntity.ok().body(nickname);
    }

}
