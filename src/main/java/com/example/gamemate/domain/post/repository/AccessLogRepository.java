package com.example.gamemate.domain.post.repository;

import com.example.gamemate.domain.post.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {


}
