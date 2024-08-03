package com.example.gamemate.domain.game.controller;

import com.example.gamemate.domain.game.dto.GameDto;
import com.example.gamemate.domain.game.service.GameApiClient;
import com.example.gamemate.domain.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameApiClient gameApiClient;

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

    @GetMapping("/fetch-games") //dto 만들어서 걔만 받아주면 됨
    public String fetchAndSaveGames(
            @RequestParam(required = false) String gametitle,
            @RequestParam(required = false) String entname,
            @RequestParam(required = false) String rateno,
            @RequestParam(required = false) String startdate,
            @RequestParam(required = false) String enddate,
            @RequestParam int display,
            @RequestParam int pageno) {

        gameService.fetchAndSaveGames(gametitle, entname, rateno, startdate, enddate, display, pageno);
        return "Games fetched and saved successfully";
    }

    //게임리스트 페이지(유저), 관리자 페이지
    @GetMapping("/games")
    public List<GameDto> getAllGames() {
        return gameService.getAllGames();
    }

    //게임 상세 페이지
    @GetMapping("/games/{id}")
    public ResponseEntity<GameDto> getGameById(@PathVariable Long id) {
        GameDto gameDto = gameService.getGameById(id);
        return ResponseEntity.ok(gameDto);
    }

    //관리자 페이지
    @PostMapping("/games")
    public ResponseEntity<GameDto> createGame(@RequestBody GameDto gameDto) {
        GameDto newGame = gameService.createGame(gameDto);
        return ResponseEntity.ok(newGame);
    }

    //관리자 페이지
    @PutMapping("/games/{id}")
    public ResponseEntity<GameDto> updateGame(@PathVariable Long id, @RequestBody GameDto gameDto) {
        GameDto updatedGame = gameService.updateGame(id, gameDto);
        return ResponseEntity.ok(updatedGame);
    }

    //관리자 페이지
    @DeleteMapping("/games/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}
