package com.example.gamemate.domain.repository;

import com.example.gamemate.domain.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {


}
