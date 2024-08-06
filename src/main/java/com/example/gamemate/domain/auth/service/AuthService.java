package com.example.gamemate.domain.auth.service;

import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.auth.dto.JoinDTO;
import com.example.gamemate.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }

    public void joinProcess(JoinDTO joinDTO) {

        Boolean isExist = userRepository.existsByUsername(joinDTO.getUsername());

        if(isExist) {

            return;

        }

        User data = new User();

        data.setUsername(joinDTO.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        data.setNickname(joinDTO.getNickname());

        userRepository.save(data);

    }
}
