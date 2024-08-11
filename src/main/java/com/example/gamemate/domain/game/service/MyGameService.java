package com.example.gamemate.domain.game.service;

import com.example.gamemate.domain.auth.dto.CustomUserDetailsDTO;
import com.example.gamemate.domain.game.dto.MyGameDto;
import com.example.gamemate.domain.game.entity.Game;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.repository.UserRepository;
import com.example.gamemate.global.exception.GameExceptionCode;
import com.example.gamemate.global.exception.RestApiException;
import com.example.gamemate.domain.game.mapper.MyGameMapper;
import com.example.gamemate.domain.game.entity.MyGame;
import com.example.gamemate.domain.game.repository.MyGameRepository;
import com.example.gamemate.domain.game.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MyGameService {

    private final MyGameRepository myGameRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final MyGameMapper myGameMapper;

    public MyGameService(MyGameRepository myGameRepository, GameRepository gameRepository, UserRepository userRepository, MyGameMapper myGameMapper) {
        this.myGameRepository = myGameRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.myGameMapper = myGameMapper;
    }

    public Page<MyGameDto> getGameListByUsername(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RestApiException(GameExceptionCode.USER_NOT_FOUND);
        }
        Page<MyGame> myGames = myGameRepository.findByUserId(user.getId(), pageable);
        return myGames.map(myGameMapper::toDto);
    }

    @Transactional
    public MyGameDto addGameToUserList(CustomUserDetailsDTO userDetailsDTO, Long gameId) {
        String username = userDetailsDTO.getUsername();
        User user = userRepository.findByUsername(username);

        if (user == null || gameId == null) {
            throw new RestApiException(GameExceptionCode.INVALID_INPUT);
        }

        if (myGameRepository.existsByUserIdAndGameId(user.getId(), gameId)) {
            throw new RestApiException(GameExceptionCode.GAME_ALREADY_EXISTS);
        }

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_NOT_FOUND));

        MyGame myGame = MyGame.builder()
                .user(user)
                .game(game)
                .build();

        MyGame savedMyGame = myGameRepository.save(myGame);
        return myGameMapper.toDto(savedMyGame);
    }

    public void deleteGameFromUserList(CustomUserDetailsDTO userDetailsDTO, Long gameId) {
        String username = userDetailsDTO.getUsername();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RestApiException(GameExceptionCode.USER_NOT_FOUND);
        }

        MyGame myGame = myGameRepository.findByUserIdAndGameId(user.getId(), gameId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_NOT_FOUND));

        myGameRepository.delete(myGame);
    }
}