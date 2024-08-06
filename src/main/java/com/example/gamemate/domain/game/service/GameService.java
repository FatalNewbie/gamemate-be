package com.example.gamemate.domain.game.service;

import com.example.gamemate.domain.game.dto.GameApiResponse;
import com.example.gamemate.domain.game.dto.GameDto;
import com.example.gamemate.global.exception.GameExceptionCode;
import com.example.gamemate.global.exception.RestApiException;
import com.example.gamemate.domain.game.mapper.GameMapper;
import com.example.gamemate.domain.game.entity.Game;
import com.example.gamemate.domain.game.repository.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final GameApiClient gameApiClient;
    private final GameRepository gameRepository;
    private final ObjectMapper objectMapper;
    private final GameMapper gameMapper;

    public GameService(GameApiClient gameApiClient, GameRepository gameRepository, ObjectMapper objectMapper, GameMapper gameMapper) {
        this.gameApiClient = gameApiClient;
        this.gameRepository = gameRepository;
        this.objectMapper = objectMapper;
        this.gameMapper = gameMapper;
    }

    public void fetchAndSaveGames(String gametitle, String entname, String rateno, String startdate, String enddate, int display, int pageno) {
        String jsonResponse = gameApiClient.fetchGames(gametitle, entname, rateno, startdate, enddate, display, pageno);
        logger.debug("Fetched response: {}", jsonResponse);

        if (jsonResponse != null) {
            try {
                GameApiResponse response = objectMapper.readValue(jsonResponse, GameApiResponse.class);

                if (response != null && response.getResult() != null && response.getResult().getItems() != null) {
                    for (GameApiResponse.GameItem item : response.getResult().getItems()) {
                        Game game = gameMapper.toEntity(item);
                        gameRepository.save(game);
                        logger.debug("Game saved: {}", game);
                    }
                } else {
                    logger.warn("No games found in the response");
                }
            } catch (IOException e) {
                logger.error("Failed to parse JSON response", e);
            }
        }
    }

    public Page<GameDto> getAllGames(Pageable pageable) {
        return gameRepository.findAllByDeletedDateIsNull(pageable)
                .map(gameMapper::toDto);
    }

    public GameDto getGameById(Long id) {
        Game game = gameRepository.findByIdAndDeletedDateIsNull(id)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_NOT_FOUND));
        return gameMapper.toDto(game);
    }

    public GameDto createGame(GameDto gameDto) {
        Game game = gameMapper.toEntity(gameDto);
        Game savedGame = gameRepository.save(game);
        return gameMapper.toDto(savedGame);
    }

    public GameDto updateGame(Long id, GameDto gameDto) {
        Game game = gameRepository.findByIdAndDeletedDateIsNull(id)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_NOT_FOUND));
        gameMapper.updateEntityFromDto(gameDto, game);
        Game updatedGame = gameRepository.save(game);
        return gameMapper.toDto(updatedGame);
    }

    public void deleteGame(Long id) {
        Game game = gameRepository.findByIdAndDeletedDateIsNull(id)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_NOT_FOUND));
        game.setDeletedDate(LocalDateTime.now());
        gameRepository.save(game);
    }
}
