package com.example.gamemate.domain.user.repository;

import com.example.gamemate.domain.user.entity.PlayTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayTimeRepository extends JpaRepository<PlayTime, Long> {
    Optional<PlayTime> findByTimeSlot(String timeSlot);
}
