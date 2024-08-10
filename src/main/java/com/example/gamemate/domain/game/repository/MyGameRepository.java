package com.example.gamemate.domain.game.repository;

import com.example.gamemate.domain.game.entity.MyGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyGameRepository extends JpaRepository<MyGame, Long> {
    Page<MyGame> findByUserId(Long userId, Pageable pageable);
    Optional<MyGame> findByUserIdAndGameId(Long userId, Long gameId);
    boolean existsByUserIdAndGameId(Long userId, Long gameId);
}
