package com.example.gamemate.domain.game.service;

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
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final MyGameMapper myGameMapper;

    public MyGameService(MyGameRepository myGameRepository, UserRepository userRepository, GameRepository gameRepository, MyGameMapper myGameMapper) {
        this.myGameRepository = myGameRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.myGameMapper = myGameMapper;
    }

    public Page<MyGameDto> getGameListByUserId(Long userId, Pageable pageable) {
        Page<MyGame> myGames = myGameRepository.findByUserId(userId, pageable);
        return myGames.map(myGameMapper::toDto);
    }

    @Transactional
    public MyGameDto addGameToUserList(MyGameDto myGameDto) {

        System.out.println("User ID: " + myGameDto.getUserId());
        System.out.println("Game ID: " + myGameDto.getGameId());

        // 1. DTO에서 필수 값 체크
        if (myGameDto.getUserId() == null || myGameDto.getGameId() == null) {
            throw new RestApiException(GameExceptionCode.INVALID_INPUT);
        }

        // 2. 해당 사용자가 이미 게임을 추가했는지 확인
        boolean exists = myGameRepository.existsByUserIdAndGameId(myGameDto.getUserId(), myGameDto.getGameId());
        if (exists) {
            throw new RestApiException(GameExceptionCode.GAME_ALREADY_EXISTS);
        }

        // 3. User와 Game 엔티티를 데이터베이스에서 조회
        User user = userRepository.findById(myGameDto.getUserId())
                .orElseThrow(() -> new RestApiException(GameExceptionCode.USER_NOT_FOUND));

        Game game = gameRepository.findById(myGameDto.getGameId())
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_NOT_FOUND));

        // 4. MyGame 엔티티 생성 및 관련 엔티티 설정
        MyGame myGame = new MyGame();
        myGame.setUser(user); // 조회된 User 설정
        myGame.setGame(game); // 조회된 Game 설정

        // 5. MyGame 엔티티 저장
        MyGame savedMyGame = myGameRepository.save(myGame);

        // 6. 저장된 MyGame 엔티티를 DTO로 변환하여 반환
        return myGameMapper.toDto(savedMyGame);
    }

    public void deleteGameFromUserList(Long userId, Long gameId) {
        MyGame myGame = myGameRepository.findByUserIdAndGameId(userId, gameId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_NOT_FOUND));
        myGameRepository.delete(myGame);
    }
}
