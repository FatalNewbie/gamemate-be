package com.example.gamemate.domain.game.mapper;

import com.example.gamemate.domain.game.dto.GameCommentDto;
import com.example.gamemate.domain.game.entity.GameComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GameCommentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.nickname", target = "nickname")
    @Mapping(source = "game.id", target = "gameId")
    GameCommentDto toDto(GameComment comment);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "gameId", target = "game.id")
    GameComment toEntity(GameCommentDto commentDto);

    void updateEntityFromDto(GameCommentDto commentDto, @MappingTarget GameComment comment);
}
