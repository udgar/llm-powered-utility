package com.llm.powered.app.service;

import com.llm.powered.app.tool.LoggingTools;
import com.openai.errors.BadRequestException;
import com.openai.errors.OpenAIInvalidDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class LlmChatService {

    private Logger LOG = LoggerFactory.getLogger(LlmChatService.class);

    private final ChatClient chatClient;
    private final String SYSTEM_PROMPT;
    private final LoggingTools loggingTools;

    public LlmChatService(ChatClient.Builder chatClientBuilder
            , @Value("classpath:prompt.txt") Resource promptResource, LoggingTools loggingTools) throws IOException {
        this.chatClient = chatClientBuilder.build();
        this.SYSTEM_PROMPT = systemPrompt(promptResource);
        this.loggingTools = loggingTools;
    }

    public String response(String prompt) {
        String response = chatClient.prompt().system(SYSTEM_PROMPT).user(prompt).call().content();
        assert response != null;
        try {
            String toolsResponse = chatClient.prompt().system(systemPrompt()).user(response).tools(loggingTools).call().content();
            LOG.info("The LLM response was persisted to db " + toolsResponse);
        } catch (BadRequestException e) {
            LOG.error("status={} body={}", e.statusCode(), e.body(), e);
        } catch (OpenAIInvalidDataException e) {
            LOG.error("body={}", e.getMessage(), e);
        }
        return response;
    }

    private String systemPrompt() {
        return """
                From the given response from LLM create a generate a log type,for example success, server error,too many request error.
                And one line summary of the response, , for example message summary was returned to the user, there was error while processing user requests, too many requests were encountered.
                """;
    }

    private String systemPrompt(Resource promptResource) throws IOException {
        assert promptResource != null;
        return promptResource.getContentAsString(StandardCharsets.UTF_8);
    }
}
