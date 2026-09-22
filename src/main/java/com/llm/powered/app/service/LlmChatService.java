package com.llm.powered.app.service;

import com.llm.powered.app.model.LoggingDto;
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

    private final Logger LOG = LoggerFactory.getLogger(LlmChatService.class);

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
        try {
            LoggingDto response = chatClient.prompt().system(SYSTEM_PROMPT).user(prompt).call().entity(LoggingDto.class);
            if (response != null) {
                loggingTools.loggingTool(response);
                LOG.info("Response from LLM successfully logged" + response);
                return String.format("%s \n %s", response.getSummary(), response.getDetails());
            } else {
                LOG.error("Null response was propagated through LLM");
                throw new RuntimeException("Null response was propagated through LLM");
            }
        } catch (BadRequestException e) {
            LOG.error("status={} body={}", e.statusCode(), e.body(), e);
        } catch (OpenAIInvalidDataException e) {
            LOG.error("body={}", e.getMessage(), e);
        }
        return "Client/Server Error Encountered";
    }

    private String systemPrompt(Resource promptResource) throws IOException {
        assert promptResource != null;
        return promptResource.getContentAsString(StandardCharsets.UTF_8);
    }
}
