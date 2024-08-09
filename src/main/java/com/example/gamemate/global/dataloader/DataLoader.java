package com.example.gamemate.global.dataloader;

import com.example.gamemate.domain.user.entity.Genre;
import com.example.gamemate.domain.user.entity.PlayTime;
import com.example.gamemate.domain.user.repository.GenreRepository;
import com.example.gamemate.domain.user.repository.PlayTimeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final GenreRepository genreRepository;
    private final PlayTimeRepository playTimeRepository;

    public DataLoader(GenreRepository genreRepository, PlayTimeRepository playTimeRepository) {
        this.genreRepository = genreRepository;
        this.playTimeRepository = playTimeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadGenres();
        loadPlayTimes();
    }

    private void loadGenres() {
        List<String> genres = Arrays.asList("FPS", "RPG", "전략", "액션", "시뮬레이션");
        for (String name : genres) {
            Optional<Genre> genreOptional = genreRepository.findByName(name);
            if (genreOptional.isEmpty()) {
                Genre genre = new Genre();
                genre.setName(name);
                genreRepository.save(genre);
            }
        }
    }

    private void loadPlayTimes() {
        List<String> playTimes = Arrays.asList("AM 9:00 ~ AM 11:00", "AM 11:00 ~ PM 2:00", "PM 2:00 ~ PM 5:00",
                "PM 5:00 ~ PM 8:00", "PM 8:00 ~ PM 11:00", "PM 11:00 ~ AM 3:00",
                "AM 3:00 ~ AM 9:00");
        for (String timeSlot : playTimes) {
            Optional<PlayTime> playTimeOptional = playTimeRepository.findByTimeSlot(timeSlot);
            if (playTimeOptional.isEmpty()) {
                PlayTime playTime = new PlayTime();
                playTime.setTimeSlot(timeSlot);
                playTimeRepository.save(playTime);
            }
        }
    }
}
