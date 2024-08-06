package com.example.gamemate.domain.game.service;

import com.example.gamemate.domain.game.dto.MyGameDto;
import com.example.gamemate.global.exception.GameExceptionCode;
import com.example.gamemate.global.exception.RestApiException;
import com.example.gamemate.domain.game.mapper.MyGameMapper;
import com.example.gamemate.domain.game.entity.MyGame;
import com.example.gamemate.domain.game.repository.MyGameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MyGameService {

    private final MyGameRepository myGameRepository;
    private final MyGameMapper myGameMapper;

    public MyGameService(MyGameRepository myGameRepository, MyGameMapper myGameMapper) {
        this.myGameRepository = myGameRepository;
        this.myGameMapper = myGameMapper;
    }

    public Page<MyGameDto> getGameListByUserId(Long userId, Pageable pageable) {
        Page<MyGame> myGames = myGameRepository.findByUserId(userId, pageable);
        return myGames.map(myGameMapper::toDto);
    }

    public MyGameDto addGameToUserList(MyGameDto myGameDto) {
        boolean exists = myGameRepository.existsByUserIdAndGameId(myGameDto.getUserId(), myGameDto.getGameId());
        if (exists) {
            throw new RestApiException(GameExceptionCode.GAME_ALREADY_EXISTS);
        }

        MyGame myGame = myGameMapper.toEntity(myGameDto);
        MyGame savedMyGame = myGameRepository.save(myGame);
        return myGameMapper.toDto(savedMyGame);
    }

    public void deleteGameFromUserList(Long userId, Long gameId) {
        MyGame myGame = myGameRepository.findByUserIdAndGameId(userId, gameId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_NOT_FOUND));
        myGameRepository.delete(myGame);
    }
}
