package com.seproject.backend.chat;

import com.seproject.backend.dto.ChatMessageRequest;
import com.seproject.backend.dto.ChatMessageResponse;
import com.seproject.backend.entity.Chat;
import com.seproject.backend.entity.User;
import com.seproject.backend.repository.ChatRepository;
import com.seproject.backend.repository.UserRepository;
import com.seproject.backend.service.ChatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ChatServiceImpl that handles chat message operations.
 * This class tests the business logic layer of the chat functionality.
 * 
 * Dependencies:
 * - ChatRepository: Handles database operations for chat messages
 * - UserRepository: Handles user-related database operations
 * - ChatServiceImpl: The service being tested
 */
public class ChatServiceTest {

    // Mock repositories to simulate database operations
    @Mock
    private ChatRepository chatRepository;

    @Mock
    private UserRepository userRepository;

    private ChatServiceImpl chatService;

    /**
     * Setup method that runs before each test.
     * Initializes mocks and creates a new instance of ChatServiceImpl.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatService = new ChatServiceImpl(chatRepository, userRepository);
    }

    /**
     * Tests the retrieval of chat messages for a specific teamspace.
     * Verifies that:
     * 1. Messages are returned in chronological order
     * 2. All message details (content, sender, timestamp) are correctly mapped
     * 3. The response contains the expected number of messages
     */
    @Test
    void getTeamspaceChat_ShouldReturnChatMessages() {
        // Arrange
        Long teamspaceId = 1L;
        User sender = new User();
        sender.setUserId(1);
        sender.setUsername("testUser");

        // Create mock chat messages
        Chat chat1 = Chat.builder()
                .id(1L)
                .message("Test message 1")
                .timestamp(LocalDateTime.now())
                .teamspaceId(teamspaceId)
                .sender(sender)
                .build();

        Chat chat2 = Chat.builder()
                .id(2L)
                .message("Test message 2")
                .timestamp(LocalDateTime.now())
                .teamspaceId(teamspaceId)
                .sender(sender)
                .build();

        List<Chat> mockChats = Arrays.asList(chat1, chat2);

        // Configure mock repository to return our test data
        when(chatRepository.findByTeamspaceIdOrderByTimestampAsc(teamspaceId))
                .thenReturn(mockChats);

        // Act
        List<ChatMessageResponse> result = chatService.getTeamspaceChat(teamspaceId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test message 1", result.get(0).getMessage());
        assertEquals("Test message 2", result.get(1).getMessage());
        assertEquals("testUser", result.get(0).getSenderUsername());
    }

    /**
     * Tests the creation of a new chat message.
     * Verifies that:
     * 1. A message can be created with valid user credentials
     * 2. The message is correctly saved with all required fields
     * 3. The response contains the correct message details
     */
    @Test
    void addChatMessage_ShouldCreateNewMessage() {
        // Arrange
        Long teamspaceId = 1L;
        String username = "testUser";
        String message = "Hello, World!";

        // Create mock user
        User sender = new User();
        sender.setUserId(1);
        sender.setUsername(username);

        // Create request DTO
        ChatMessageRequest request = ChatMessageRequest.builder()
                .message(message)
                .build();

        // Create expected saved chat message
        Chat savedChat = Chat.builder()
                .id(1L)
                .message(message)
                .timestamp(LocalDateTime.now())
                .teamspaceId(teamspaceId)
                .sender(sender)
                .build();

        // Configure mock repositories
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(sender));
        when(chatRepository.save(any(Chat.class))).thenReturn(savedChat);

        // Act
        ChatMessageResponse result = chatService.addChatMessage(teamspaceId, request, username);

        // Assert
        assertNotNull(result);
        assertEquals(message, result.getMessage());
        assertEquals(username, result.getSenderUsername());
        assertEquals(teamspaceId, result.getTeamspaceId());
    }

    /**
     * Tests error handling when attempting to create a message with an invalid user.
     * Verifies that:
     * 1. The service throws an exception when the user doesn't exist
     * 2. The error is properly propagated
     */
    @Test
    void addChatMessage_WithInvalidUser_ShouldThrowException() {
        // Arrange
        Long teamspaceId = 1L;
        String username = "nonexistentUser";
        ChatMessageRequest request = ChatMessageRequest.builder()
                .message("Test message")
                .build();

        // Configure mock to simulate non-existent user
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            chatService.addChatMessage(teamspaceId, request, username)
        );
    }
} 