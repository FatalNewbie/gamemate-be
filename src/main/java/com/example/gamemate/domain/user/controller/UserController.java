package com.example.gamemate.domain.user.controller;

import com.example.gamemate.domain.auth.dto.CustomUserDetailsDTO;
import com.example.gamemate.domain.user.dto.MyPageResponseDTO;
import com.example.gamemate.domain.user.dto.RecommendResponseDTO;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.mapper.UserMapper;
import com.example.gamemate.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
//        log.info("User role: {}", role);
//        log.info("Accessing mypage");
        System.out.println(role);
        System.out.println("mypage");

        MyPageResponseDTO myPageDto = userService.findByUsername(name);

        return ResponseEntity.ok().header("Content-Type", "application/json").body(myPageDto);
    }

    @GetMapping("/info")
    public ResponseEntity<RecommendResponseDTO> getUserInfo(
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        RecommendResponseDTO user = userService.findByUsernameForRecommendation(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}
