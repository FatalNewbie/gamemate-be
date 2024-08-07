package com.example.gamemate.domain.user.service;

import com.example.gamemate.domain.user.dto.MyPageResponseDTO;
import com.example.gamemate.domain.user.dto.RecommendResponseDTO;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.mapper.UserMapper;
import com.example.gamemate.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper mapper;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper mapper){

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mapper = mapper;

    }

    public MyPageResponseDTO findByUsername(String username) {

        User user = userRepository.findByUsername(username);

        if(!user.equals("")) {
            return mapper.userToMyPageDto(user);
        }

        return null;

    }

    public RecommendResponseDTO findByUsernameForRecommendation(String username) {
        User user = userRepository.findByUsername(username);

        if(!user.equals("")) {
            return mapper.userToRecommendResponseDTO(user);
        }
        return null;
    }

}
