package com.example.gamemate.domain.game.service;

import com.example.gamemate.domain.game.dto.GameListDto;
import com.example.gamemate.domain.game.entity.GameList;
import com.example.gamemate.domain.game.mapper.GameListMapper;
import com.example.gamemate.domain.game.repository.GameListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameListService {

    @Autowired
    private GameListRepository gameListRepository;

    @Autowired
    private GameListMapper gameListMapper;

    public List<GameListDto> getGameListByUserId(Long userId) {
        List<GameList> gameList = gameListRepository.findByUserId(userId);
        return gameList.stream()
                .map(gameListMapper::toDto)
                .collect(Collectors.toList());
    }

    public GameListDto addGameToUserList(GameListDto gameListDto) {
        // 중복 확인
        boolean exists = gameListRepository.existsByUserIdAndGameId(gameListDto.getUserId(), gameListDto.getGameId());
        if (exists) {
            throw new IllegalStateException("Game already exists in the user's list");
        }

        GameList gameList = gameListMapper.toEntity(gameListDto);
        GameList savedGameList = gameListRepository.save(gameList);
        return gameListMapper.toDto(savedGameList);
    }

    public void deleteGameFromUserList(Long userId, Long gameId) {
        GameList gameList = gameListRepository.findByUserIdAndGameId(userId, gameId)
                .orElseThrow(() -> new RuntimeException("Game not found in user's list"));
        gameListRepository.delete(gameList);
    }
}
