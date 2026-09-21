package com.llm.powered.app.tool;

import com.llm.powered.app.entity.LoggingEntity;
import com.llm.powered.app.repository.LoggingJpaRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class LoggingTools {

    private final LoggingJpaRepository jpaRepository;

    public LoggingTools(LoggingJpaRepository repository) {
        this.jpaRepository = repository;
    }

    @Tool(name = "response_logging_tool", description = "Persist the log type and a one-line summary of an LLM response to the database")
    public String loggingTool(@ToolParam(description = "Type of the response, e.g. success, server error, too many request error") String logType,
                            @ToolParam(description = "One-line summary of the response, e.g. message summary was returned to the user") String logSummary) {
        var entity=jpaRepository.save(new LoggingEntity(logType, logSummary));
        return entity.toString();
    }

    private static @NonNull LoggingEntity getLoggingEntity(String logType, String logSummary) {
        return new LoggingEntity(logType, logSummary);
    }

}
