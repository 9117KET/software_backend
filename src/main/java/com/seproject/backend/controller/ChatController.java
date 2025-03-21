package com.seproject.backend.controller;

import com.seproject.backend.dto.ChatMessageRequest;
import com.seproject.backend.dto.ChatMessageResponse;
import com.seproject.backend.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller that exposes chat endpoints for teamspaces.
 * 
 * This controller handles HTTP requests related to chat messaging within teamspaces.
 * It follows the REST architecture pattern and uses Spring MVC annotations to map
 * HTTP requests to handler methods.
 * 
 * Key aspects of this controller:
 * 1. Provides endpoints to get chat messages and add new messages
 * 2. Uses path variables to identify the teamspace context for messages
 * 3. Utilizes Spring Security for authentication
 * 4. Returns HTTP status codes appropriate to each operation
 * 5. Includes Swagger documentation for API consumption
 * 
 * Design patterns:
 * - Follows Controller pattern in MVC architecture
 * - Uses Dependency Injection for service access
 * - Implements RESTful design principles
 */
@RestController
@RequestMapping("/api/teamspaces/{teamspaceId}/chat")
@Tag(name = "Chat", description = "Chat API for teamspaces")
public class ChatController {

    /**
     * Service that provides chat operations. Injected by Spring
     */
    private final ChatService chatService;

    /**
     * Constructor that enables dependency injection of the chat service.
     * Spring will automatically inject the ChatService implementation at runtime.
     * 
     * @param chatService The service that handles chat business logic
     */
    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Endpoint to retrieve all chat messages for a specific teamspace.
     * 
     * This endpoint maps to HTTP GET requests and returns a list of chat messages
     * for the specified teamspace. Messages are ordered by timestamp ascending.
     * 
     * URL pattern: GET /api/teamspaces/{teamspaceId}/chat
     * 
     * Authentication: Required (inherited from security configuration)
     * Authorization: Determined by security rules (user must have access to the teamspace)
     * 
     * @param teamspaceId ID of the teamspace to retrieve chat messages for
     * @return HTTP 200 OK with a list of chat messages if successful
     */
    @GetMapping
    @Operation(summary = "Get chat messages for a teamspace", 
               description = "Retrieve all chat messages for a specific teamspace ordered by timestamp")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved chat messages",
                    content = @Content(schema = @Schema(implementation = ChatMessageResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Teamspace not found")
    })
    public ResponseEntity<List<ChatMessageResponse>> getTeamspaceChat(
            @Parameter(description = "ID of the teamspace to retrieve chat messages for") 
            @PathVariable Long teamspaceId) {
        List<ChatMessageResponse> chatMessages = chatService.getTeamspaceChat(teamspaceId);
        return ResponseEntity.ok(chatMessages);
    }

    /**
     * Endpoint to add a new chat message to a teamspace.
     * 
     * This endpoint maps to HTTP POST requests and creates a new chat message
     * in the specified teamspace. The message is associated with the authenticated user.
     * 
     * URL pattern: POST /api/teamspaces/{teamspaceId}/chat
     * 
     * Authentication: Required (Spring Security extracts user details from Authentication object)
     * Authorization: Determined by security rules (user must have access to post in the teamspace)
     * 
     * @param teamspaceId ID of the teamspace to add the chat message to
     * @param chatMessageRequest Request body containing the chat message content
     * @param authentication Spring Security authentication object containing user details
     * @return HTTP 201 Created with the created chat message if successful
     */
    @PostMapping
    @Operation(summary = "Add a new chat message", 
               description = "Add a new chat message to a specific teamspace")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Chat message successfully added",
                    content = @Content(schema = @Schema(implementation = ChatMessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid message data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Teamspace not found")
    })
    public ResponseEntity<ChatMessageResponse> addChatMessage(
            @Parameter(description = "ID of the teamspace to add the chat message to") 
            @PathVariable Long teamspaceId,
            @Parameter(description = "Chat message details") 
            @RequestBody ChatMessageRequest chatMessageRequest,
            Authentication authentication) {
        // Extract username from the authentication object
        String username = authentication.getName();
        
        // Delegate to service to create the message
        ChatMessageResponse chatMessage = chatService.addChatMessage(teamspaceId, chatMessageRequest, username);
        
        // Return HTTP 201 Created with the created message
        return new ResponseEntity<>(chatMessage, HttpStatus.CREATED);
    }
} 