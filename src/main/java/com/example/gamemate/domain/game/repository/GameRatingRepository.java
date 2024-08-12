package com.example.gamemate.domain.game.repository;

import com.example.gamemate.domain.game.entity.GameRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRatingRepository extends JpaRepository<GameRating, Long> {
    // 특정 게임의 평점 목록을 가져오는 메서드
    List<GameRating> findByGameId(Long gameId);

    // 특정 사용자가 특정 게임에 대해 작성한 평점을 찾는 메서드
    Optional<GameRating> findByUserIdAndGameId(Long userId, Long gameId);

    // 특정 사용자의 모든 평점을 가져오는 메서드 (추가 기능 필요시)
    List<GameRating> findByUserId(Long userId);
}