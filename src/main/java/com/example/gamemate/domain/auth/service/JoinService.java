package com.example.gamemate.domain.auth.service;

import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.auth.dto.JoinDTO;
import com.example.gamemate.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
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
        data.setPreferredGenres(preferredGenres);
        data.setPlayTimes(playTimes);
        userRepository.save(data);

    }
}
