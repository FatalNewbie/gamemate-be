package com.example.gamemate.domain.auth.service;

import com.example.gamemate.domain.user.entity.Genre;
import com.example.gamemate.domain.user.entity.PlayTime;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.auth.dto.JoinDTO;
import com.example.gamemate.domain.user.repository.GenreRepository;
import com.example.gamemate.domain.user.repository.PlayTimeRepository;
import com.example.gamemate.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final PlayTimeRepository playTimeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public JoinService(UserRepository userRepository, GenreRepository genreRepository, PlayTimeRepository playTimeRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
        this.playTimeRepository = playTimeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }

    public void joinProcess(JoinDTO joinDTO) {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        String nickname = joinDTO.getNickname();
        List<Integer> preferredGenres = joinDTO.getPreferredGenres();
        List<Integer> playTimes = joinDTO.getPlayTimes();

        Boolean isExist = userRepository.existsByUsername(username);

        if(isExist) {

            return;

        }

        User data = new User();
        // builder 사용하는 것이 좋을 듯
        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setNickname(nickname);

        List<Genre> genres = IntStream.range(0, preferredGenres.size())
                .filter(i -> preferredGenres.get(i) == 1)
                .mapToObj(i -> genreRepository.findById((long) (i + 1)).orElseThrow(() -> new IllegalArgumentException("Invalid genre ID: " + (i + 1))))
                .collect(Collectors.toList());
        data.setPreferredGenres(genres);

        List<PlayTime> times = IntStream.range(0, playTimes.size())
                .filter(i -> playTimes.get(i) == 1)
                .mapToObj(i -> playTimeRepository.findById((long) (i + 1)).orElseThrow(() -> new IllegalArgumentException("Invalid play time ID: " + (i + 1))))
                .collect(Collectors.toList());
        data.setPlayTimes(times);

        userRepository.save(data);

    }
}
