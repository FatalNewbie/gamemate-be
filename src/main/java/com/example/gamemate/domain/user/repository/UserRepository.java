package com.example.gamemate.domain.user.repository;

import com.example.gamemate.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    User findByUsername(String username);

    Boolean existsByNickname(String nickname);

}
