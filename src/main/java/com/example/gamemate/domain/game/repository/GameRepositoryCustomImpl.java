package com.example.gamemate.domain.game.repository;
import com.example.gamemate.domain.game.entity.Game;
import com.example.gamemate.domain.game.repository.GameRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.example.gamemate.domain.game.entity.QGame.game;

public class GameRepositoryCustomImpl implements GameRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public GameRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Game> findGamesByTitleAndDeveloper(String title, String developer) {
        BooleanBuilder builder = new BooleanBuilder();
        if (title != null && !title.isEmpty()) {
            builder.and(game.title.containsIgnoreCase(title));
        }
        if (developer != null && !developer.isEmpty()) {
            builder.and(game.developer.containsIgnoreCase(developer));
        }

        return queryFactory.selectFrom(game)
                .where(builder)
                .fetch();
    }
}
