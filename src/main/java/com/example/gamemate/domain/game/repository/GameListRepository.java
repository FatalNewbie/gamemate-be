package com.example.gamemate.domain.game.repository;

import com.example.gamemate.domain.game.entity.GameList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameListRepository extends JpaRepository<GameList, Long> {
    List<GameList> findByUserId(Long userId);
    Optional<GameList> findByUserIdAndGameId(Long userId, Long gameId);
    boolean existsByUserIdAndGameId(Long userId, Long gameId);
}
