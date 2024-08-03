package com.example.gamemate.domain.game.repository;

import com.example.gamemate.domain.game.entity.GameComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameCommentRepository extends JpaRepository<GameComment, Long> {
    List<GameComment> findByGameIdAndDeletedDateIsNull(Long gameId);
    Optional<GameComment> findByGameIdAndIdAndDeletedDateIsNull(Long gameId, Long commentId);
}
