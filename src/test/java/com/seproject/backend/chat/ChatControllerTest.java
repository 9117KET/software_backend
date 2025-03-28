package com.seproject.backend.chat;

import com.seproject.backend.controller.ChatController;
import com.seproject.backend.dto.ChatMessageRequest;
import com.seproject.backend.dto.ChatMessageResponse;
import com.seproject.backend.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ChatController that handles HTTP requests for chat functionality.
 * This class tests the REST API endpoints and request/response handling.
 * 
 * Dependencies:
 * - ChatService: Business logic layer for chat operations
 * - Authentication: Spring Security authentication object
 * - ChatController: The controller being tested
 */
public class ChatControllerTest {

    // Mock service to simulate business logic operations
    @Mock
    private ChatService chatService;

    // Mock authentication to simulate authenticated user
    @Mock
    private Authentication authentication;

    private ChatController chatController;

    /**
     * Setup method that runs before each test.
     * Initializes mocks and creates a new instance of ChatController.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatController = new ChatController(chatService);
    }

    /**
     * Tests the GET endpoint for retrieving chat messages.
     * Verifies that:
     * 1. The endpoint returns HTTP 200 OK status
     * 2. The response body contains the correct number of messages
     * 3. Message content and metadata are correctly mapped
     */
    @Test
    void getTeamspaceChat_ShouldReturnMessages() {
        // Arrange
        Long teamspaceId = 1L;
        
        // Create mock response messages
        ChatMessageResponse message1 = ChatMessageResponse.builder()
                .id(1L)
                .message("Test message 1")
                .timestamp(LocalDateTime.now())
                .teamspaceId(teamspaceId)
                .senderId(1L)
                .senderUsername("testUser")
                .build();

        ChatMessageResponse message2 = ChatMessageResponse.builder()
                .id(2L)
                .message("Test message 2")
                .timestamp(LocalDateTime.now())
                .teamspaceId(teamspaceId)
                .senderId(1L)
                .senderUsername("testUser")
                .build();

        List<ChatMessageResponse> mockMessages = Arrays.asList(message1, message2);

        // Configure mock service to return our test data
        when(chatService.getTeamspaceChat(teamspaceId)).thenReturn(mockMessages);

        // Act
        ResponseEntity<List<ChatMessageResponse>> response = chatController.getTeamspaceChat(teamspaceId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Test message 1", response.getBody().get(0).getMessage());
        assertEquals("Test message 2", response.getBody().get(1).getMessage());
    }

    /**
     * Tests the POST endpoint for creating a new chat message.
     * Verifies that:
     * 1. The endpoint returns HTTP 201 CREATED status
     * 2. The response body contains the created message details
     * 3. The message is associated with the correct user and teamspace
     */
    @Test
    void addChatMessage_ShouldCreateMessage() {
        // Arrange
        Long teamspaceId = 1L;
        String username = "testUser";
        String message = "Hello, World!";

        // Create request DTO (Data Transfer Object) - an object that carries data between processes
        ChatMessageRequest request = ChatMessageRequest.builder()
                .message(message)
                .build();

        // Create mock response
        ChatMessageResponse mockResponse = ChatMessageResponse.builder()
                .id(1L)
                .message(message)
                .timestamp(LocalDateTime.now())
                .teamspaceId(teamspaceId)
                .senderId(1L)
                .senderUsername(username)
                .build();

        // Configure mocks
        when(authentication.getName()).thenReturn(username);
        when(chatService.addChatMessage(eq(teamspaceId), any(ChatMessageRequest.class), eq(username)))
                .thenReturn(mockResponse);

        // Act
        ResponseEntity<ChatMessageResponse> response = chatController.addChatMessage(teamspaceId, request, authentication);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(message, response.getBody().getMessage());
        assertEquals(username, response.getBody().getSenderUsername());
        assertEquals(teamspaceId, response.getBody().getTeamspaceId());
    }
} 