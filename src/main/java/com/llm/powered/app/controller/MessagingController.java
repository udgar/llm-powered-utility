package com.llm.powered.app.controller;

import com.llm.powered.app.service.LlmChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/api/v1/messaging")
@Tag(name = "Messaging", description = "LLM-backed chat endpoints for messaging")
public class MessagingController {

    private final LlmChatService service;

    public MessagingController(LlmChatService service) {
        this.service = service;
    }

    @Operation(
            summary = "Chat with the LLM powered messaging advisor",
            description = "Sends a plain-text prompt to the LLM and provide messaging description"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Streamed LLM response",
                    content = @Content(
                            mediaType = MediaType.TEXT_EVENT_STREAM_VALUE,
                            schema = @Schema(type = "string")
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Missing or empty prompt", content = @Content),
            @ApiResponse(responseCode = "500", description = "LLM service error", content = @Content)
    })
    @PostMapping("/normalchat")
    public Flux<String> normalChat(@RequestBody String userPrompt) {
        return service.response(userPrompt);
    }
}
