package com.example.gamemate.domain.user.repository;

import com.example.gamemate.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    User findByUsername(String username);
    Optional<User> findById(Long id);

    Boolean existsByNickname(String nickname);

}
