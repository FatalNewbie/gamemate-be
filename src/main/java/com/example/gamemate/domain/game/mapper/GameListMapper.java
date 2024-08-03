package com.example.gamemate.domain.game.mapper;

import com.example.gamemate.domain.game.dto.GameListDto;
import com.example.gamemate.domain.game.entity.GameList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GameListMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "game.id", target = "gameId")
    GameListDto toDto(GameList gameList);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "gameId", target = "game.id")
    GameList toEntity(GameListDto gameListDto);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "gameId", target = "game.id")
    void updateEntityFromDto(GameListDto gameListDto, @MappingTarget GameList gameList);
}
