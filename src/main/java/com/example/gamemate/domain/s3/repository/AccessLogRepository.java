package com.example.gamemate.domain.s3.repository;

import com.example.gamemate.domain.s3.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {


}
