package com.example.gamemate.domain.game.service;

import com.example.gamemate.domain.game.dto.GameRatingDto;
import com.example.gamemate.global.exception.GameExceptionCode;
import com.example.gamemate.global.exception.RestApiException;
import com.example.gamemate.domain.game.mapper.GameRatingMapper;
import com.example.gamemate.domain.game.entity.Game;
import com.example.gamemate.domain.game.entity.GameRating;
import com.example.gamemate.domain.game.repository.GameRatingRepository;
import com.example.gamemate.domain.game.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameRatingService {

    private final GameRatingRepository gameRatingRepository;
    private final GameRatingMapper gameRatingMapper;
    private final GameRepository gameRepository;

    public GameRatingService(GameRatingRepository gameRatingRepository, GameRatingMapper gameRatingMapper, GameRepository gameRepository) {
        this.gameRatingRepository = gameRatingRepository;
        this.gameRatingMapper = gameRatingMapper;
        this.gameRepository = gameRepository;
    }

    public GameRatingDto getRatingById(Long gameId, Long ratingId) {
        GameRating rating = gameRatingRepository.findByGameIdAndId(gameId, ratingId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_RATING_NOT_FOUND));
        return gameRatingMapper.toDto(rating);
    }

    public GameRatingDto createRating(Long gameId, GameRatingDto ratingDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_NOT_FOUND));

        GameRating rating = gameRatingMapper.toEntity(ratingDto);
        rating.setGame(game);
        GameRating savedRating = gameRatingRepository.save(rating);
        return gameRatingMapper.toDto(savedRating);
    }

    public GameRatingDto updateRating(Long gameId, Long ratingId, GameRatingDto ratingDto) {
        GameRating rating = gameRatingRepository.findByGameIdAndId(gameId, ratingId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_RATING_NOT_FOUND));
        gameRatingMapper.updateEntityFromDto(ratingDto, rating);
        GameRating updatedRating = gameRatingRepository.save(rating);
        return gameRatingMapper.toDto(updatedRating);
    }

    public void deleteRating(Long gameId, Long ratingId) {
        GameRating rating = gameRatingRepository.findByGameIdAndId(gameId, ratingId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_RATING_NOT_FOUND));
        gameRatingRepository.delete(rating); // 완전 삭제
    }
}
