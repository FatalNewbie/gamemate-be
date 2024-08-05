package com.example.gamemate.domain.user.service;

import com.example.gamemate.domain.user.dto.MyPageResponseDTO;
import com.example.gamemate.domain.user.dto.UpdateDTO;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.mapper.UserMapper;
import com.example.gamemate.domain.user.repository.UserRepository;
import java.util.Optional;
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

    public MyPageResponseDTO findByUsernameForMyPage(String username) {

        User user = userRepository.findByUsername(username);

        if(!user.equals("")) {
            return mapper.userToMyPageDto(user);
        }

        return null;

    }

    public UpdateDTO findByUsernameForUpdate(String username){

        User user = userRepository.findByUsername(username);
        if(!user.equals("")){
            return mapper.userToUpdateDto(user);
        }

        return null;

    }

    public UpdateDTO update(UpdateDTO updateDTO) {
        // 사용자 엔티티 생성
        User user = mapper.updateDtoToUser(updateDTO);

        // 비밀번호 암호화
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        // 사용자 정보 업데이트
        User updatedUser = userRepository.save(user);

        // 업데이트된 사용자 DTO 반환
        return mapper.userToUpdateDto(updatedUser);
    }

    public void deletedByUsername(String username) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setDeleted(true);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public void restorationByUsername(String username) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setDeleted(false);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

}
