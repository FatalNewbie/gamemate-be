package com.example.gamemate.domain.game.repository;

import com.example.gamemate.domain.game.entity.GameRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRatingRepository extends JpaRepository<GameRating, Long> {
    List<GameRating> findByGameId(Long gameId);
    Optional<GameRating> findByGameIdAndId(Long gameId, Long ratingId);
}
