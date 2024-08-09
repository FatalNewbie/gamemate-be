package com.example.gamemate.domain.game.controller;

import com.example.gamemate.domain.game.dto.GameRatingDto;
import com.example.gamemate.domain.game.service.GameRatingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Game Rating", description = "Game Rating API")
@Slf4j
@RestController
@RequestMapping("/games/{gameId}/ratings")
public class GameRatingController {

    private final GameRatingService gameRatingService;

    public GameRatingController(GameRatingService gameRatingService) {
        this.gameRatingService = gameRatingService;
    }


    @GetMapping("/{ratingId}")
    public ResponseEntity<GameRatingDto> getRatingById(@PathVariable Long gameId, @PathVariable Long ratingId) {
        GameRatingDto ratingDto = gameRatingService.getRatingById(gameId, ratingId);
        return ResponseEntity.ok(ratingDto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<GameRatingDto> createRating(
            @PathVariable Long gameId,
            @Valid @RequestBody GameRatingDto ratingDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 사용자 ID를 ratingDto에 설정하거나 다른 처리를 여기에 추가할 수 있습니다.
        GameRatingDto newRating = gameRatingService.createRating(gameId, ratingDto);
        return ResponseEntity.ok(newRating);
    }

    @PutMapping("/{ratingId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<GameRatingDto> updateRating(
            @PathVariable Long gameId,
            @PathVariable Long ratingId,
            @Valid @RequestBody GameRatingDto ratingDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        GameRatingDto updatedRating = gameRatingService.updateRating(gameId, ratingId, ratingDto);
        return ResponseEntity.ok(updatedRating);
    }

    @Transactional
    @DeleteMapping("/{ratingId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> deleteRating(
            @PathVariable Long gameId,
            @PathVariable Long ratingId,
            @AuthenticationPrincipal UserDetails userDetails) {

        gameRatingService.deleteRating(gameId, ratingId);
        return ResponseEntity.ok().build();
    }
}
