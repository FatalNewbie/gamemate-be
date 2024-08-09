package com.example.gamemate.domain.game.controller;

import com.example.gamemate.global.common.CustomPage;
import com.example.gamemate.domain.game.dto.GameCommentDto;
import com.example.gamemate.domain.game.service.GameCommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    // page 그대로 리턴 x 리스트만 뱉어줄 수 있게끔 특정 클래스 만들어서 페이징   오브젝트 상속
    @GetMapping
    public ResponseEntity<CustomPage<GameCommentDto>> getAllComments(
            @PathVariable Long gameId,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {

        Pageable pageable = Pageable.unpaged();
        if (page.isPresent() && size.isPresent()) {
            pageable = PageRequest.of(page.get(), size.get());
        }

        Page<GameCommentDto> comments = gameCommentService.getAllComments(gameId, pageable);
        CustomPage<GameCommentDto> customPage = new CustomPage<>(comments);
        return ResponseEntity.ok(customPage);
    }



    // 특정 코멘트 조회
    @GetMapping("/{commentId}")
    public ResponseEntity<GameCommentDto> getCommentById(@PathVariable Long gameId, @PathVariable Long commentId) {
        GameCommentDto commentDto = gameCommentService.getCommentById(gameId, commentId);
        return ResponseEntity.ok(commentDto);
    }

    // 코멘트 생성
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<GameCommentDto> createComment(
            @PathVariable Long gameId,
            @Valid @RequestBody GameCommentDto commentDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 사용자 ID를 commentDto에 설정하거나 다른 처리를 여기에 추가할 수 있습니다.
        GameCommentDto newComment = gameCommentService.createComment(gameId, commentDto);
        return ResponseEntity.ok(newComment);
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<GameCommentDto> updateComment(
            @PathVariable Long gameId,
            @PathVariable Long commentId,
            @Valid @RequestBody GameCommentDto commentDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        GameCommentDto updatedComment = gameCommentService.updateComment(gameId, commentId, commentDto);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long gameId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {

        gameCommentService.deleteComment(gameId, commentId);
        return ResponseEntity.ok().build();
    }
}
