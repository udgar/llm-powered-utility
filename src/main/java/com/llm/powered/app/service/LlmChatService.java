package com.llm.powered.app.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class LlmChatService {

    private final ChatClient chatClient;
    private final String SYSTEM_PROMPT;

    public LlmChatService(ChatClient.Builder chatClientBuilder
            , @Value("classpath:prompt.txt") Resource promptResource) throws IOException {
        this.chatClient = chatClientBuilder.build();
        this.SYSTEM_PROMPT = systemPrompt(promptResource);
    }

    public Flux<String> response(String prompt) {
        return chatClient.prompt().system(SYSTEM_PROMPT).user(prompt).stream().content();
    }

    private String systemPrompt(Resource promptResource) throws IOException {
        assert promptResource != null;
        return promptResource.getContentAsString(StandardCharsets.UTF_8);
    }
}
