package com.llm.powered.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "LOG")
public class LoggingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LOG_TYPE")
    private String logType;
    @Column(name = "LOG_SUMMARY")
    private String logSummary;

    public LoggingEntity() {
    }

    public LoggingEntity(String logType, String logSummary) {
        this.logType = logType;
        this.logSummary = logSummary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getLogSummary() {
        return logSummary;
    }

    public void setLogSummary(String logSummary) {
        this.logSummary = logSummary;
    }

    @Override
    public String toString() {
        return "LoggingEntity{" +
                ", logType='" + logType + '\'' +
                ", logSummary='" + logSummary + '\'' +
                '}';
    }
}
