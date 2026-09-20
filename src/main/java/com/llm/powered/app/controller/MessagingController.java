package com.llm.powered.app.controller;

import com.llm.powered.app.service.LlmChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/api/v1/messaging")
public class MessagingController {

    private final LlmChatService service;

    public MessagingController(LlmChatService service) {
        this.service = service;
    }

    @GetMapping("/normalchat")
    public Flux<String> normalChat(@RequestBody String userPrompt) {
        return service.response(userPrompt);
    }
}
