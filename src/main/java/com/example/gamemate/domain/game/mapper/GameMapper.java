package com.example.gamemate.domain.game.mapper;

import com.example.gamemate.domain.game.dto.GameApiResponse;
import com.example.gamemate.domain.game.dto.GameDto;
import com.example.gamemate.domain.game.entity.Game;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GameMapper {

    GameDto toDto(Game game);

    Game toEntity(GameDto gameDto);

    @Mapping(source = "gametitle", target = "title")
    @Mapping(source = "entname", target = "developer")
    @Mapping(source = "genre", target = "genre")
    @Mapping(source = "platform", target = "platform")
    @Mapping(source = "givenrate", target = "classes")
    @Mapping(source = "summary", target = "description")
    Game toEntity(GameApiResponse.GameItem gameItem);

    void updateEntityFromDto(GameDto gameDto, @MappingTarget Game game);
}
