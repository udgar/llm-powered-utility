package com.llm.powered.app.model;


public class LoggingDto {

    private String responseCategory;

    private String details;

    private String summary;

    public LoggingDto() {
    }

    public LoggingDto(String responseCategory, String response, String summary) {
        this.responseCategory = responseCategory;
        this.details = response;
        this.summary = summary;
    }

    public String getResponseCategory() {
        return responseCategory;
    }

    public void setResponseCategory(String responseCategory) {
        this.responseCategory = responseCategory;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
