package com.example.gamemate.domain.game.controller;

import com.example.gamemate.domain.auth.dto.CustomUserDetailsDTO;
import com.example.gamemate.global.apiRes.ApiResponse;
import com.example.gamemate.global.common.CustomPage;
import com.example.gamemate.domain.game.dto.MyGameDto;
import com.example.gamemate.domain.game.service.MyGameService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "My Game", description = "My Game API")
@Slf4j
@RestController
@RequestMapping("/games")
public class MyGameController {

    private final MyGameService myGameService;

    public MyGameController(MyGameService myGameService) {
        this.myGameService = myGameService;
    }

    @GetMapping("/my-games")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<CustomPage<MyGameDto>> getUserGameList(
            @AuthenticationPrincipal CustomUserDetailsDTO customUserDetailsDTO,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        String username = customUserDetailsDTO.getUsername();
        log.info("Fetching game list for username: {}", username);

        Page<MyGameDto> myGames = myGameService.getGameListByUsername(username, pageable);
        CustomPage<MyGameDto> customPage = new CustomPage<>(myGames);
        return ApiResponse.successRes(HttpStatus.OK, customPage);
    }

    @PostMapping("/my-games/{gameId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<MyGameDto> addGameToUserList(
            @AuthenticationPrincipal CustomUserDetailsDTO customUserDetailsDTO,
            @PathVariable Long gameId) {

        String username = customUserDetailsDTO.getUsername();
        log.info("Adding game ID: {} to username: {}'s list", gameId, username);

        MyGameDto createdGame = myGameService.addGameToUserList(customUserDetailsDTO, gameId);
        return ApiResponse.successRes(HttpStatus.CREATED, createdGame);
    }

    @DeleteMapping("/my-games/{gameId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<Void> deleteGameFromUserList(
            @AuthenticationPrincipal CustomUserDetailsDTO customUserDetailsDTO,
            @PathVariable Long gameId) {

        String username = customUserDetailsDTO.getUsername();
        log.info("Deleting game ID: {} from username: {}'s list", gameId, username);

        myGameService.deleteGameFromUserList(customUserDetailsDTO, gameId);
        return ApiResponse.successRes(HttpStatus.NO_CONTENT, null);
    }
}