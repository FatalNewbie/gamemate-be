package com.example.gamemate.domain.game.service;

import com.example.gamemate.domain.game.dto.CommentDto;
import com.example.gamemate.domain.game.dto.GameApiResponse;
import com.example.gamemate.domain.game.dto.GameDto;
import com.example.gamemate.domain.game.dto.RatingDto;
import com.example.gamemate.global.exception.GameExceptionCode;
import com.example.gamemate.global.exception.RestApiException;
import com.example.gamemate.domain.game.mapper.GameMapper;
import com.example.gamemate.domain.game.entity.Game;
import com.example.gamemate.domain.game.repository.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public Page<GameDto> findGamesByTitleAndDeveloper(String title, String developer, Pageable pageable) {
        Page<Game> games = gameRepository.findGamesByTitleAndDeveloper(title, developer, pageable);
        return games.map(gameMapper::toDto);
    }

    public Page<GameDto> getAllGames(Pageable pageable) {

        if (pageable.isUnpaged()) {
            List<Game> games = gameRepository.findAllByDeletedDateIsNull(); // 페이징 없이 모든 데이터 가져오기
            return new PageImpl<>(games.stream().map(gameMapper::toDto).collect(Collectors.toList()));
        } else {
            return gameRepository.findAllByDeletedDateIsNull(pageable)
                    .map(gameMapper::toDto);
        }
    }

    public Page<GameDto> getFilteredGames(Pageable pageable, String filter) {

        Page<Game> gamePage;
        if(filter.isEmpty()){
            gamePage = gameRepository.findAll(pageable);
        }else {
            gamePage = gameRepository.findGameByPlatform(filter, pageable);
        }

        // Page<GameDto>로 변환하여 반환
        return gamePage.map(gameMapper::toDto);
    }

    public GameDto getGameById(Long id) {
        Game game = gameRepository.findByIdAndDeletedDateIsNull(id)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_NOT_FOUND));
        // comments와 ratings를 각각 매핑하여 GameDto로 변환
        List<CommentDto> comments = gameMapper.toCommentDtoList(game.getComments());
        List<RatingDto> ratings = gameMapper.toRatingDtoList(game.getRatings());

        GameDto gameDto = gameMapper.toDto(game);

        // GameDto에 comments와 ratings를 설정
        gameDto.setComments(comments);
        gameDto.setRatings(ratings);

        return gameDto;
    }

    public GameDto createGame(GameDto gameDto) {
        Game game = gameMapper.toEntity(gameDto);
        Game savedGame = gameRepository.save(game);
        return gameMapper.toDto(savedGame);
    }

    public GameDto updateGame(Long id, GameDto gameDto) {
        // 기존 엔티티를 DB에서 조회
        Game game = gameRepository.findByIdAndDeletedDateIsNull(id)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_NOT_FOUND));

        // DTO의 ID를 제외한 나머지 필드들로 엔티티를 업데이트
        game.setTitle(gameDto.getTitle());
        game.setDeveloper(gameDto.getDeveloper());
        game.setDescription(gameDto.getDescription());
        game.setClasses(gameDto.getClasses());
        game.setGenre(gameDto.getGenre());
        game.setPlatform(gameDto.getPlatform());

        // 업데이트된 엔티티를 저장
        Game updatedGame = gameRepository.save(game);

        // 엔티티를 DTO로 변환하여 반환
        return gameMapper.toDto(updatedGame);
    }

    public void deleteGame(Long id) {
        Game game = gameRepository.findByIdAndDeletedDateIsNull(id)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_NOT_FOUND));
        game.setDeletedDate(LocalDateTime.now());
        gameRepository.save(game);
    }
}
