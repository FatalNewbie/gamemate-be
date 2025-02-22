package com.example.gamemate.domain.user.controller;

import com.example.gamemate.domain.auth.dto.CustomUserDetailsDTO;
import com.example.gamemate.domain.friend.service.FriendService;
import com.example.gamemate.domain.game.service.GameCommentService;
import com.example.gamemate.domain.game.service.GameRatingService;
import com.example.gamemate.domain.post.service.PostService;
import com.example.gamemate.domain.user.dto.MyPageResponseDTO;
import com.example.gamemate.domain.user.dto.RecommendResponseDTO;
import com.example.gamemate.domain.user.dto.UpdateDTO;
import com.example.gamemate.domain.user.mapper.UserMapper;
import com.example.gamemate.domain.user.service.UserService;
import com.example.gamemate.global.apiRes.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "게임메이트 유저 API")
@Slf4j
@RestController
@ResponseBody
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final GameCommentService gameCommentService;
    private final GameRatingService gameRatingService;
    private final FriendService friendService;
    private final UserMapper mapper;

    public UserController(
        UserService userService,
        PostService postService,
        GameCommentService gameCommentService,
        GameRatingService gameRatingService,
        FriendService friendService,
        UserMapper mapper
    ) {

        this.userService = userService;
        this.postService = postService;
        this.gameCommentService = gameCommentService;
        this.gameRatingService = gameRatingService;
        this.friendService = friendService;
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
        // 현재 인증된 사용자 이름을 가져옵니다.
        String currentUsername = userDetails.getUsername();

        // UpdateDTO에 현재 사용자의 사용자명을 설정
        updateDTO.setUsername(currentUsername);

        // 사용자 정보 업데이트
        UpdateDTO updatedUser = userService.update(updateDTO);

        return "수정완료";
    }

    @GetMapping("/delete")
    public ResponseEntity<String> deleteByName(@RequestParam("username") String username) {
        // 로그 추가
        log.info("deleteCheck");

        //게시글 삭제
        postService.deletePostsByUsername(username);
        log.info("deletePostsByUsername");

        // 댓글 삭제
        gameCommentService.deleteCommentsByUsername(username);
        log.info("deleteCommentsByUsername");

        // 평점 삭제
        gameRatingService.deleteUserRatingsByUsername(username);
        log.info("deleteUserRatingsByUsername");

        //친구 관계 삭제
        friendService.deleteUserRelatedFriend(username);
        log.info("deleteUserRelatedFriend");

        // 유저 삭제
        userService.deletedByUsername(username);
        log.info("deletedByUsername");

        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/restoreUser/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public String restoreUser(@PathVariable String username) {
        userService.restorationByUsername(username); // userService에서 회원 복구 작업을 처리
        return "복구완료"; // 복구 성공 메시지 반환
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


    // 겟 요청시 닉네임 반환, 이삭 추가
    @GetMapping("/user")
    public ResponseEntity<String> getUserNickname(@AuthenticationPrincipal UserDetails userDetails){
        String nickname = userService.findByUsernameForMyPage(userDetails.getUsername()).getNickname();
        return ResponseEntity.ok().body(nickname);
    }

}
