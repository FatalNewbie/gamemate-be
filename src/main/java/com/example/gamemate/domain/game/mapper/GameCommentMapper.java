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
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.userProfile", target = "userProfile") // userProfile 매핑
    @Mapping(source = "game.id", target = "gameId")
    GameCommentDto toDto(GameComment comment);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "gameId", target = "game.id")
    @Mapping(target = "user.nickname", ignore = true) // If you don't want to map this directly
    @Mapping(target = "user.username", ignore = true) // If you don't want to map this directly
    GameComment toEntity(GameCommentDto commentDto);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "gameId", target = "game.id")
    @Mapping(target = "user.nickname", ignore = true) // If you don't want to map this directly
    @Mapping(target = "user.username", ignore = true) // If you don't want to map this directly
    void updateEntityFromDto(GameCommentDto commentDto, @MappingTarget GameComment comment);
}
