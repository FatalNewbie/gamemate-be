package com.example.gamemate.domain.game.repository;

import com.example.gamemate.domain.game.entity.Game;
import java.util.List;

public interface GameRepositoryCustom {
    List<Game> findGamesByTitleAndDeveloper(String title, String developer);
}
