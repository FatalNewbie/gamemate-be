package com.example.gamemate.domain.game.repository;

import com.example.gamemate.domain.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByDeletedDateIsNull();
    Optional<Game> findByIdAndDeletedDateIsNull(Long id);
}
//페이지네이션 적용 autowired 적용