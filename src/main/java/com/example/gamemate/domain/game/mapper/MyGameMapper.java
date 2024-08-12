package com.example.gamemate.domain.game.mapper;

import com.example.gamemate.domain.game.dto.GameDto;
import com.example.gamemate.domain.game.dto.MyGameDto;
import com.example.gamemate.domain.game.entity.Game;
import com.example.gamemate.domain.game.entity.MyGame;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = GameMapper.class)
public interface MyGameMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "game", target = "game", qualifiedByName = "toBasicGameDto")
    MyGameDto toDto(MyGame myGame);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "game.id", target = "game.id")
    MyGame toEntity(MyGameDto myGameDto);

    void updateEntityFromDto(MyGameDto myGameDto, @MappingTarget MyGame myGame);
}