package com.example.gamemate.domain.game.controller;

import com.example.gamemate.domain.auth.dto.CustomUserDetailsDTO;
import com.example.gamemate.global.apiRes.ApiResponse;
import com.example.gamemate.global.common.CustomPage;
import com.example.gamemate.domain.game.dto.GameCommentDto;
import com.example.gamemate.domain.game.service.GameCommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Game Comment", description = "Game Comment API")
@Slf4j
@RestController
@RequestMapping("/games/{gameId}/comments")
public class GameCommentController {

    private final GameCommentService gameCommentService;

    public GameCommentController(GameCommentService gameCommentService) {
        this.gameCommentService = gameCommentService;
    }

    // 댓글 리스트 조회
    @GetMapping
    public ApiResponse<CustomPage<GameCommentDto>> getAllComments(
            @PathVariable Long gameId,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {

        log.info("Fetching comments for game ID: {}", gameId);

        Pageable pageable = Pageable.unpaged();
        if (page.isPresent() && size.isPresent()) {
            pageable = PageRequest.of(page.get(), size.get(), Sort.by(Sort.Direction.DESC, "createdDate"));
        }

        Page<GameCommentDto> comments = gameCommentService.getAllComments(gameId, pageable);
        CustomPage<GameCommentDto> customPage = new CustomPage<>(comments);
        return ApiResponse.successRes(HttpStatus.OK, customPage);
    }
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<GameCommentDto> createComment(
            @AuthenticationPrincipal CustomUserDetailsDTO customUserDetailsDTO,
            @PathVariable Long gameId,
            @Valid @RequestBody GameCommentDto commentDto) {

        log.info("Creating comment for game ID: {} by user: {}", gameId, customUserDetailsDTO.getUsername());

        // commentDto에는 사용자 이름을 설정하지 않아도 됩니다. 이는 서비스에서 처리됩니다.
        GameCommentDto newComment = gameCommentService.createUserCommentForGame(customUserDetailsDTO, gameId, commentDto);
        return ApiResponse.successRes(HttpStatus.CREATED, newComment);
    }


    @PutMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<GameCommentDto> updateComment(
            @AuthenticationPrincipal CustomUserDetailsDTO customUserDetailsDTO,
            @PathVariable Long gameId,
            @PathVariable Long commentId,
            @Valid @RequestBody GameCommentDto commentDto) {

        String username = customUserDetailsDTO.getUsername();
        log.info("Updating comment ID: {} for game ID: {} by user: {}", commentId, gameId, username);

        GameCommentDto updatedComment = gameCommentService.updateComment(gameId, commentId, username, commentDto);
        return ApiResponse.successRes(HttpStatus.OK, updatedComment);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<Void> deleteComment(
            @AuthenticationPrincipal CustomUserDetailsDTO customUserDetailsDTO,
            @PathVariable Long gameId,
            @PathVariable Long commentId) {

        String username = customUserDetailsDTO.getUsername();

        log.info("Deleting comment ID: {} for game ID: {} by user: {}", commentId, gameId, username);

        gameCommentService.deleteComment(gameId, commentId, username);
        return ApiResponse.successRes(HttpStatus.NO_CONTENT, null);
    }
}