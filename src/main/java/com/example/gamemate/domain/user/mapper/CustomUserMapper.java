package com.example.gamemate.domain.user.mapper;

import com.example.gamemate.domain.user.dto.UpdateDTO;
import com.example.gamemate.domain.user.entity.Genre;
import com.example.gamemate.domain.user.entity.PlayTime;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.repository.GenreRepository;
import com.example.gamemate.domain.user.repository.PlayTimeRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomUserMapper {

    private final GenreRepository genreRepository;
    private final PlayTimeRepository playTimeRepository;

    public CustomUserMapper(GenreRepository genreRepository, PlayTimeRepository playTimeRepository) {
        this.genreRepository = genreRepository;
        this.playTimeRepository = playTimeRepository;
    }

    public User updateDtoToUser(UpdateDTO updateDTO, User existingUser) {
        if (updateDTO.getNickname() != null) {
            existingUser.setNickname(updateDTO.getNickname());
        }

        if (updateDTO.getPassword() != null) {
            existingUser.setPassword(updateDTO.getPassword());
        }

        if (updateDTO.getPreferredGenres() != null) {
            List<Genre> genres = updateDTO.getPreferredGenres().stream()
                    .map(id -> genreRepository.findById(id.longValue())
                            .orElseThrow(() -> new RuntimeException("Genre not found for ID: " + id)))
                    .collect(Collectors.toList());
            existingUser.setPreferredGenres(genres);
        }

        if (updateDTO.getPlayTimes() != null) {
            List<PlayTime> playTimes = updateDTO.getPlayTimes().stream()
                    .map(id -> playTimeRepository.findById(id.longValue())
                            .orElseThrow(() -> new RuntimeException("PlayTime not found for ID: " + id)))
                    .collect(Collectors.toList());
            existingUser.setPlayTimes(playTimes);
        }

        return existingUser;
    }

    public UpdateDTO userToUpdateDto(User user) {
        UpdateDTO updateDTO = new UpdateDTO();
        updateDTO.setUsername(user.getUsername());
        updateDTO.setNickname(user.getNickname());
        updateDTO.setPassword(user.getPassword());

        // 엔티티 리스트를 ID 리스트로 변환
        updateDTO.setPreferredGenres(user.getPreferredGenres().stream()
                .map(Genre::getId)
                .map(Long::intValue)
                .collect(Collectors.toList()));

        updateDTO.setPlayTimes(user.getPlayTimes().stream()
                .map(PlayTime::getId)
                .map(Long::intValue)
                .collect(Collectors.toList()));

        return updateDTO;
    }
}

