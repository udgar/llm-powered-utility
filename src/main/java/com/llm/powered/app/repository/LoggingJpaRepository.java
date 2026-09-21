package com.llm.powered.app.repository;

import com.llm.powered.app.entity.LoggingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggingJpaRepository extends JpaRepository<LoggingEntity,Long> {
}
