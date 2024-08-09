package com.example.gamemate.domain.game.repository;

import com.example.gamemate.domain.game.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>, GameRepositoryCustom {
    Page<Game> findAllByDeletedDateIsNull(Pageable pageable);
    List<Game> findAllByDeletedDateIsNull(); // 새로운 메서드 추가
    Optional<Game> findByIdAndDeletedDateIsNull(Long id);
}