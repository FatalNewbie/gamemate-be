package com.example.gamemate.domain.game.mapper;

import com.example.gamemate.domain.game.dto.GameRatingDto;
import com.example.gamemate.domain.game.entity.GameRating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GameRatingMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "game.id", target = "gameId")
    GameRatingDto toDto(GameRating rating);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "gameId", target = "game.id")
    GameRating toEntity(GameRatingDto ratingDto);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "gameId", target = "game.id")
    void updateEntityFromDto(GameRatingDto ratingDto, @MappingTarget GameRating rating);
}
