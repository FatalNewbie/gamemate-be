package com.example.gamemate.domain.game.repository;

import com.example.gamemate.domain.game.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameRepositoryCustom {
    Page<Game> findGamesByTitleAndDeveloper(String title, String developer, Pageable pageable);
}
