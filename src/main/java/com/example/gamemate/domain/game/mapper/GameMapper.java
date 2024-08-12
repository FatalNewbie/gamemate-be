package com.example.gamemate.domain.game.mapper;

import com.example.gamemate.domain.game.dto.CommentDto;
import com.example.gamemate.domain.game.dto.GameApiResponse;
import com.example.gamemate.domain.game.dto.GameDto;
import com.example.gamemate.domain.game.dto.RatingDto;
import com.example.gamemate.domain.game.entity.Game;
import com.example.gamemate.domain.game.entity.GameComment;
import com.example.gamemate.domain.game.entity.GameRating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(source = "gametitle", target = "title")
    @Mapping(source = "entname", target = "developer")
    @Mapping(source = "genre", target = "genre")
    @Mapping(source = "platform", target = "platform")
    @Mapping(source = "givenrate", target = "classes")
    @Mapping(source = "summary", target = "description")
    Game toEntity(GameApiResponse.GameItem gameItem);

    @Named("toBasicGameDto")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    GameDto toBasicDto(Game game);

    GameDto toDto(Game game);

    Game toEntity(GameDto gameDto);

    void updateEntityFromDto(GameDto gameDto, @MappingTarget Game game);

    CommentDto toCommentDto(GameComment comment);

    RatingDto toRatingDto(GameRating rating);

    List<CommentDto> toCommentDtoList(List<GameComment> comments);

    List<RatingDto> toRatingDtoList(List<GameRating> ratings);
}
