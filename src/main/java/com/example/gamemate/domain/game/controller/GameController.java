package com.example.gamemate.domain.game.controller;

import com.example.gamemate.domain.game.dto.GameDto;
import com.example.gamemate.domain.game.service.GameApiClient;
import com.example.gamemate.domain.game.service.GameService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Game", description = "Game API")
@Slf4j
@RestController
@RequestMapping
public class GameController {

    private final GameService gameService;
    private final GameApiClient gameApiClient;

    public GameController(GameService gameService, GameApiClient gameApiClient) {
        this.gameService = gameService;
        this.gameApiClient = gameApiClient;
    }

    //게임 api fetch 확인 api
    @GetMapping("/fetch-and-convert-games")
    public String fetchAndConvertGames(
            @RequestParam(required = false) String gametitle,
            @RequestParam(required = false) String entname,
            @RequestParam(required = false) String rateno,
            @RequestParam(required = false) String startdate,
            @RequestParam(required = false) String enddate,
            @RequestParam int display,
            @RequestParam int pageno) {

        return gameApiClient.fetchGames(gametitle, entname, rateno, startdate, enddate, display, pageno);
    }

    //게임 api 데이터베이스 추가 api
    @GetMapping("/fetch-games")
    public String fetchAndSaveGames(
            @RequestParam(required = false) String gametitle,
            @RequestParam(required = false) String entname,
            @RequestParam(required = false) String rateno,
            @RequestParam(required = false) String startdate,
            @RequestParam(required = false) String enddate,
            @RequestParam int display,
            @RequestParam int pageno) {

        gameService.fetchAndSaveGames(gametitle, entname, rateno, startdate, enddate, display, pageno);
        return "게임 정보가 성공적으로 저장되었습니다.";
    }

    // 게임 리스트 조회
    @GetMapping("/games")
    public ResponseEntity<Page<GameDto>> getAllGames(@PageableDefault(size = 10) Pageable pageable) {
        Page<GameDto> games = gameService.getAllGames(pageable);
        return ResponseEntity.ok(games);
    }

    //게임상세 조회 api
    @GetMapping("/games/{id}")
    public ResponseEntity<GameDto> getGameById(@PathVariable Long id) {
        GameDto gameDto = gameService.getGameById(id);
        return ResponseEntity.ok(gameDto);
    }

    //게임상세 추가 api (관리자)
    @PostMapping("/games")
    public ResponseEntity<GameDto> createGame(@RequestBody GameDto gameDto) {
        GameDto newGame = gameService.createGame(gameDto);
        return ResponseEntity.ok(newGame);
    }

    //게임상세 수정 api (관리자)
    @PutMapping("/games/{id}")
    public ResponseEntity<GameDto> updateGame(@PathVariable Long id, @RequestBody GameDto gameDto) {
        GameDto updatedGame = gameService.updateGame(id, gameDto);
        return ResponseEntity.ok(updatedGame);
    }

    // 게임상세 삭제 api (관리자)
    @DeleteMapping("/games/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.ok().build();
    }
}
