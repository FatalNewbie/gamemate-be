package com.example.gamemate.domain.user.mapper;

import com.example.gamemate.domain.user.dto.MyPageResponseDTO;
import com.example.gamemate.domain.user.dto.RecommendResponseDTO;
import com.example.gamemate.domain.user.entity.Genre;
import com.example.gamemate.domain.user.entity.PlayTime;
import com.example.gamemate.domain.user.entity.User;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    MyPageResponseDTO userToMyPageDto(User user);
    @Mapping(target = "preferredGenres", source = "preferredGenres", qualifiedByName = "genresToIds")
    @Mapping(target = "playTimes", source = "playTimes", qualifiedByName = "playTimesToIds")
    RecommendResponseDTO userToRecommendResponseDTO(User user);

    @Named("genresToIds")
    default List<Long> genresToIds(List<Genre> genres) {
        return genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toList());
    }

    @Named("playTimesToIds")
    default List<Long> playTimesToIds(List<PlayTime> playTimes) {
        return playTimes.stream()
                .map(PlayTime::getId)
                .collect(Collectors.toList());
    }

}
