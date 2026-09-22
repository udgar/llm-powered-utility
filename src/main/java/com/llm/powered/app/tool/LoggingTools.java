package com.llm.powered.app.tool;

import com.llm.powered.app.entity.LoggingEntity;
import com.llm.powered.app.model.LoggingDto;
import com.llm.powered.app.repository.LoggingJpaRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class LoggingTools {

    private final LoggingJpaRepository jpaRepository;

    public LoggingTools(LoggingJpaRepository repository) {
        this.jpaRepository = repository;
    }

    public void loggingTool(LoggingDto dto) {
        jpaRepository.save(new LoggingEntity(dto.getResponseCategory(), dto.getSummary()));
    }

    private static @NonNull LoggingEntity getLoggingEntity(String logType, String logSummary) {
        return new LoggingEntity(logType, logSummary);
    }

}
