package com.example.gamemate.domain.game.controller;

import com.example.gamemate.domain.auth.dto.CustomUserDetailsDTO;
import com.example.gamemate.domain.game.dto.GameRatingDto;
import com.example.gamemate.domain.game.service.GameRatingService;
import com.example.gamemate.global.apiRes.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Game Rating", description = "Game Rating API")
@Slf4j
@RestController
@RequestMapping("/games/{gameId}/ratings")
public class GameRatingController {

    private final GameRatingService gameRatingService;

    public GameRatingController(GameRatingService gameRatingService) {
        this.gameRatingService = gameRatingService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<GameRatingDto> getUserGameRating(
            @AuthenticationPrincipal CustomUserDetailsDTO customUserDetailsDTO,
            @PathVariable Long gameId) {

        String username = customUserDetailsDTO.getUsername();
        log.info("Fetching rating for game ID: {} by user: {}", gameId, username);

        GameRatingDto ratingDto = gameRatingService.getUserRatingForGame(username, gameId);
        return ApiResponse.successRes(HttpStatus.OK, ratingDto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<GameRatingDto> createGameRating(
            @AuthenticationPrincipal CustomUserDetailsDTO customUserDetailsDTO,
            @PathVariable Long gameId,
            @RequestBody GameRatingDto ratingDto) {

        String username = customUserDetailsDTO.getUsername();
        log.info("Creating rating for game ID: {} by user: {}", gameId, username);

        GameRatingDto newRating = gameRatingService.createUserRatingForGame(username, gameId, ratingDto);
        return ApiResponse.successRes(HttpStatus.CREATED, newRating);
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<GameRatingDto> updateGameRating(
            @AuthenticationPrincipal CustomUserDetailsDTO customUserDetailsDTO,
            @PathVariable Long gameId,
            @RequestBody GameRatingDto ratingDto) {

        String username = customUserDetailsDTO.getUsername();
        log.info("Updating rating for game ID: {} by user: {}", gameId, username);

        GameRatingDto updatedRating = gameRatingService.updateUserRatingForGame(username, gameId, ratingDto);
        return ApiResponse.successRes(HttpStatus.OK, updatedRating);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<Void> deleteGameRating(
            @AuthenticationPrincipal CustomUserDetailsDTO customUserDetailsDTO,
            @PathVariable Long gameId) {

        String username = customUserDetailsDTO.getUsername();
        log.info("Deleting rating for game ID: {} by user: {}", gameId, username);

        gameRatingService.deleteUserRatingForGame(username, gameId);
        return ApiResponse.successRes(HttpStatus.NO_CONTENT, null);
    }
}
