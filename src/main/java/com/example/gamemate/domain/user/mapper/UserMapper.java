package com.example.gamemate.domain.user.mapper;

import com.example.gamemate.domain.user.dto.MyPageResponseDTO;
import com.example.gamemate.domain.user.dto.RecommendResponseDTO;
import com.example.gamemate.domain.user.entity.Genre;
import com.example.gamemate.domain.user.entity.PlayTime;
import com.example.gamemate.domain.user.dto.UpdateDTO;
import com.example.gamemate.domain.user.entity.User;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    MyPageResponseDTO userToMyPageDto(User user);
    // 기존 방식에서 CustomUserMapper를 직접 호출하여 매핑 처리
    default UpdateDTO userToUpdateDto(User user, CustomUserMapper customUserMapper) {
        return customUserMapper.userToUpdateDto(user);
    }

    default User updateDtoToUser(UpdateDTO updateDTO, CustomUserMapper customUserMapper, User existingUser) {
        return customUserMapper.updateDtoToUser(updateDTO, existingUser);
    }
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
