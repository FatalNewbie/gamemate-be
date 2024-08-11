package com.example.gamemate.domain.game.service;

import com.example.gamemate.domain.game.dto.GameRatingDto;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.repository.UserRepository;
import com.example.gamemate.global.exception.GameExceptionCode;
import com.example.gamemate.global.exception.RestApiException;
import com.example.gamemate.domain.game.mapper.GameRatingMapper;
import com.example.gamemate.domain.game.entity.Game;
import com.example.gamemate.domain.game.entity.GameRating;
import com.example.gamemate.domain.game.repository.GameRatingRepository;
import com.example.gamemate.domain.game.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
@Service
public class GameRatingService {

    private final GameRatingRepository gameRatingRepository;
    private final GameRatingMapper gameRatingMapper;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public GameRatingService(GameRatingRepository gameRatingRepository, GameRatingMapper gameRatingMapper, GameRepository gameRepository, UserRepository userRepository) {
        this.gameRatingRepository = gameRatingRepository;
        this.gameRatingMapper = gameRatingMapper;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public GameRatingDto getUserRatingForGame(String username, Long gameId) {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RestApiException(GameExceptionCode.USER_NOT_FOUND);
        }

        GameRating rating = gameRatingRepository.findByUserIdAndGameId(user.getId(), gameId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_RATING_NOT_FOUND));

        return gameRatingMapper.toDto(rating);
    }


    @Transactional
    public GameRatingDto createUserRatingForGame(String username, Long gameId, GameRatingDto ratingDto) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RestApiException(GameExceptionCode.USER_NOT_FOUND);
        }

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_NOT_FOUND));

        GameRating rating = gameRatingMapper.toEntity(ratingDto);
        rating.setUser(user);
        rating.setGame(game);

        GameRating savedRating = gameRatingRepository.save(rating);
        return gameRatingMapper.toDto(savedRating);
    }

    @Transactional
    public GameRatingDto updateUserRatingForGame(String username, Long gameId, GameRatingDto ratingDto) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RestApiException(GameExceptionCode.USER_NOT_FOUND);
        }

        GameRating rating = gameRatingRepository.findByUserIdAndGameId(user.getId(), gameId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_RATING_NOT_FOUND));

        gameRatingMapper.updateEntityFromDto(ratingDto, rating);
        GameRating updatedRating = gameRatingRepository.save(rating);
        return gameRatingMapper.toDto(updatedRating);
    }

    @Transactional
    public void deleteUserRatingForGame(String username, Long gameId) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RestApiException(GameExceptionCode.USER_NOT_FOUND);
        }

        GameRating rating = gameRatingRepository.findByUserIdAndGameId(user.getId(), gameId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_RATING_NOT_FOUND));

        gameRatingRepository.delete(rating);
    }
}