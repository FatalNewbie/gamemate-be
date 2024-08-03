package com.example.gamemate.domain.game.controller;

import com.example.gamemate.domain.game.dto.GameListDto;
import com.example.gamemate.domain.game.service.GameListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/games")
public class GameListController {

    @Autowired
    private GameListService gameListService;

    //마이페이지 Pathvariable 그냥 빼면됨 여기는
    @GetMapping
    public List<GameListDto> getUserGameList(@PathVariable Long userId) {
        return gameListService.getGameListByUserId(userId);
    }

    //게임상세 페이지 > 내 게임에 추가
    @PostMapping("/{gameId}")
    public ResponseEntity<GameListDto> addGameToUserList(@PathVariable Long userId, @PathVariable Long gameId) {
        GameListDto gameListDto = new GameListDto();
        gameListDto.setUserId(userId);
        gameListDto.setGameId(gameId);
        GameListDto savedGame = gameListService.addGameToUserList(gameListDto);
        return ResponseEntity.ok(savedGame);
    }

    //게임상세 페이지 > 내 게임에서 삭제
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGameFromUserList(@PathVariable Long userId, @PathVariable Long gameId) {
        gameListService.deleteGameFromUserList(userId, gameId);
        return ResponseEntity.noContent().build();
    }
}
