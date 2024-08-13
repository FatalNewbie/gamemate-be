package com.example.gamemate.domain.game.repository;
import com.example.gamemate.domain.game.repository.GameRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import com.example.gamemate.domain.game.entity.Game;

import java.util.List;

import static com.example.gamemate.domain.game.entity.QGame.game;

@Repository
public class GameRepositoryCustomImpl implements GameRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public GameRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Game> findGamesByTitleAndDeveloper(String title, String developer, Pageable pageable) {
        if (pageable.isUnpaged()) {
            // pageable이 Unpaged인 경우 전체 결과를 반환
            List<Game> games = queryFactory
                    .selectFrom(game)
                    .where(titleContains(title), developerContains(developer))
                    .fetch();

            return new PageImpl<>(games);
        } else {
            // pageable이 Unpaged가 아닌 경우 페이징 처리된 결과를 반환
            List<Game> games = queryFactory
                    .selectFrom(game)
                    .where(titleContains(title), developerContains(developer))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            long total = queryFactory
                    .selectFrom(game)
                    .where(titleContains(title), developerContains(developer))
                    .fetchCount();

            return new PageImpl<>(games, pageable, total);
        }
    }


    private BooleanExpression titleContains(String title) {
        return title != null ? game.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression developerContains(String developer) {
        return developer != null ? game.developer.containsIgnoreCase(developer) : null;
    }
}
