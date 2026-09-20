package com.llm.powered.app.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class LlmChatService {

    private final ChatClient chatClient;

    public LlmChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public Flux<String> response(String prompt) {
        return chatClient.prompt().user(prompt).stream().content();
    }
}
