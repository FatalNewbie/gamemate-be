package com.example.gamemate.domain.game.controller;

import com.example.gamemate.global.apiRes.ApiResponse;
import com.example.gamemate.global.common.CustomPage;
import com.example.gamemate.domain.game.dto.GameDto;
import com.example.gamemate.domain.game.service.GameApiClient;
import com.example.gamemate.domain.game.service.GameService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ApiResponse<String> fetchAndConvertGames(
            @RequestParam(required = false) String gametitle,
            @RequestParam(required = false) String entname,
            @RequestParam(required = false) String rateno,
            @RequestParam(required = false) String startdate,
            @RequestParam(required = false) String enddate,
            @RequestParam int display,
            @RequestParam int pageno) {

        log.info("Fetching and converting games with title: {}, entname: {}, rateno: {}", gametitle, entname, rateno);
        String result = gameApiClient.fetchGames(gametitle, entname, rateno, startdate, enddate, display, pageno);
        return ApiResponse.successRes(HttpStatus.OK, result);
    }

    //게임 api 데이터베이스 추가 api
    @GetMapping("/fetch-games")
    public ApiResponse<String> fetchAndSaveGames(
            @RequestParam(required = false) String gametitle,
            @RequestParam(required = false) String entname,
            @RequestParam(required = false) String rateno,
            @RequestParam(required = false) String startdate,
            @RequestParam(required = false) String enddate,
            @RequestParam int display,
            @RequestParam int pageno) {

        log.info("Fetching and saving games with title: {}, entname: {}, rateno: {}", gametitle, entname, rateno);
        gameService.fetchAndSaveGames(gametitle, entname, rateno, startdate, enddate, display, pageno);
        return ApiResponse.successRes(HttpStatus.OK, "게임 정보가 성공적으로 저장되었습니다.");
    }

    @GetMapping("/games")
    public ApiResponse<CustomPage<GameDto>> getAllGames(
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> developer,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size
    ) {

        log.info("Fetching games with optional title: {} and developer: {}", title.orElse(""), developer.orElse(""));

        Pageable pageable = Pageable.unpaged(); // 기본적으로 모든 데이터를 가져오는 설정
        if (page.isPresent() && size.isPresent()) {
            pageable = PageRequest.of(page.get(), size.get());
        }

        Page<GameDto> games;

        if (title.isPresent() || developer.isPresent()) {
            // 타이틀이나 개발자 이름이 제공되면 검색 실행
            games = gameService.findGamesByTitleAndDeveloper(title.orElse(""), developer.orElse(""), pageable);
        } else {
            // 검색 조건이 없으면 모든 게임 리스트 조회
            games = gameService.getAllGames(pageable);
        }


        CustomPage<GameDto> customPage = new CustomPage<>(games);
        return ApiResponse.successRes(HttpStatus.OK, customPage);
    }

    @GetMapping("/games/search")
    public ApiResponse<CustomPage<GameDto>> getFilteredGames(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size
    ) {

        Pageable pageable = Pageable.unpaged(); // 기본적으로 모든 데이터를 가져오는 설정
        if (page.isPresent() && size.isPresent()) {
            pageable = PageRequest.of(page.get(), size.get());
        }

        Page<GameDto> games;

        games = gameService.getFilteredGames(pageable, filter);

        CustomPage<GameDto> customPage = new CustomPage<>(games);
        return ApiResponse.successRes(HttpStatus.OK, customPage);
    }



    //게임상세 조회 api
    @GetMapping("/games/{id}")
    public ApiResponse<GameDto> getGameById(@PathVariable Long id) {
        log.info("Fetching game with ID: {}", id);
        GameDto gameDto = gameService.getGameById(id);
        return ApiResponse.successRes(HttpStatus.OK, gameDto);
    }

    //게임상세 추가 api (관리자)
    @PostMapping("/games")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<GameDto> createGame(@RequestBody GameDto gameDto) {
        log.info("Creating new game with title: {}", gameDto.getTitle());
        GameDto newGame = gameService.createGame(gameDto);
        return ApiResponse.successRes(HttpStatus.OK, newGame);
    }

    //게임상세 수정 api (관리자)
    @PutMapping("/games/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<GameDto> updateGame(@PathVariable Long id, @RequestBody GameDto gameDto) {
        log.info("Updating game with ID: {}", id);
        GameDto updatedGame = gameService.updateGame(id, gameDto);
        return ApiResponse.successRes(HttpStatus.OK, updatedGame);
    }

    // 게임상세 삭제 api (관리자)
    @DeleteMapping("/games/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<Void> deleteGame(@PathVariable Long id) {
        log.info("Deleting game with ID: {}", id);
        gameService.deleteGame(id);
        return ApiResponse.successRes(HttpStatus.NO_CONTENT, null);
    }
}