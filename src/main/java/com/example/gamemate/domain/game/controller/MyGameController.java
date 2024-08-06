package com.example.gamemate.domain.game.controller;

import com.example.gamemate.domain.auth.dto.CustomUserDetailsDTO;
import com.example.gamemate.domain.game.dto.MyGameDto;
import com.example.gamemate.domain.game.service.MyGameService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "My Game", description = "My Game API")
@Slf4j
@RestController
@RequestMapping("/games")
public class MyGameController {

    private final MyGameService myGameService;

    public MyGameController(MyGameService myGameService) {
        this.myGameService = myGameService;
    }

    // 마이페이지 - 현재 로그인한 사용자의 게임 리스트 조회
    @GetMapping("/my-games")
    public ResponseEntity<Page<MyGameDto>> getUserGameList(@AuthenticationPrincipal CustomUserDetailsDTO customUserDetailsDTO,
                                                           @PageableDefault(size = 10) Pageable pageable) {
        Long userId = customUserDetailsDTO.getId();
        Page<MyGameDto> myGames = myGameService.getGameListByUserId(userId, pageable);
        return ResponseEntity.ok(myGames);
    }

    // 게임상세 페이지 > 내 게임에 추가
    @PostMapping("/my-games/{gameId}")
    public ResponseEntity<MyGameDto> addGameToUserList(@AuthenticationPrincipal CustomUserDetailsDTO customUserDetailsDTO, @PathVariable Long gameId) {
        Long userId = customUserDetailsDTO.getId();
        MyGameDto myGameDto = new MyGameDto();
        myGameDto.setUserId(userId);
        myGameDto.setGameId(gameId);
        MyGameDto savedGame = myGameService.addGameToUserList(myGameDto);
        return ResponseEntity.ok(savedGame);
    }

    // 게임상세 페이지 > 내 게임에서 삭제
    @DeleteMapping("/my-games/{gameId}")
    public ResponseEntity<Void> deleteGameFromUserList(@AuthenticationPrincipal CustomUserDetailsDTO customUserDetailsDTO, @PathVariable Long gameId) {
        Long userId = customUserDetailsDTO.getId();
        myGameService.deleteGameFromUserList(userId, gameId);
        return ResponseEntity.ok().build();
    }
}
