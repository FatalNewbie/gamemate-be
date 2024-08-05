package com.example.gamemate.domain.user.controller;

import com.example.gamemate.domain.auth.dto.CustomUserDetailsDTO;
import com.example.gamemate.domain.user.dto.MyPageResponseDTO;
import com.example.gamemate.domain.user.dto.UpdateDTO;
import com.example.gamemate.domain.user.mapper.UserMapper;
import com.example.gamemate.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
        log.info(role);
        log.info("Accessing mypage");
//        System.out.println(role);
//        System.out.println("mypage");

        MyPageResponseDTO myPageDto = userService.findByUsernameForMyPage(name);

        return ResponseEntity.ok().header("Content-Type", "application/json").body(myPageDto);
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

    @GetMapping("/api/v1/delete")
    public String deleteByName(@RequestParam("username") String username) {
        // 로그 추가
        log.info("deleteCheck");

        userService.deletedByUsername(username);
        return "삭제완료";
    }


    @PutMapping("/api/v1/restoreUser/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public String restoreUser(@PathVariable String username) {
        userService.restorationByUsername(username); // userService에서 회원 복구 작업을 처리
        return "복구완료"; // 복구 성공 메시지 반환
    }

}
