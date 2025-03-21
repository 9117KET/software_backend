package com.seproject.backend.service;

import com.seproject.backend.dto.ChatMessageRequest;
import com.seproject.backend.dto.ChatMessageResponse;
import com.seproject.backend.entity.Chat;
import com.seproject.backend.entity.User;
import com.seproject.backend.repository.ChatRepository;
import com.seproject.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ChatService interface that provides the chat functionality.
 * 
 * This service handles all business logic related to chat messages, acting as a
 * mediator between the web layer (controllers) and the data access layer (repositories).
 * It handles operations such as retrieving chat messages and adding new messages.
 * 
 * This class leverages Spring's dependency injection to obtain references to the
 * repositories it needs. The @Service annotation registers this as a Spring service
 * bean, making it available for autowiring in other components.
 */
@Service
public class ChatServiceImpl implements ChatService {

    /**
     * Repository for chat message data access operations
     */
    private final ChatRepository chatRepository;
    
    /**
     * Repository for user data access operations, needed to resolve senders
     */
    private final UserRepository userRepository;

    /**
     * Constructor that injects the required repositories.
     * Spring will automatically inject implementations of these repositories
     * at runtime using dependency injection.
     * 
     * @param chatRepository Repository for chat operations
     * @param userRepository Repository for user operations
     */
    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     * 
     * Implementation details:
     * 1. Fetches chat messages from the repository using the teamspace ID
     * 2. Converts each Chat entity to a ChatMessageResponse DTO
     * 3. Returns the list of DTOs to the client
     */
    @Override
    public List<ChatMessageResponse> getTeamspaceChat(Long teamspaceId) {
        // Retrieve messages for the teamspace ordered by timestamp
        List<Chat> chats = chatRepository.findByTeamspaceIdOrderByTimestampAsc(teamspaceId);
        
        // Convert each Chat entity to a ChatMessageResponse DTO using the Stream API
        return chats.stream()
                .map(this::mapChatToResponse)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     * 
     * Implementation details:
     * 1. Finds the user by username from the user repository
     * 2. Creates a new Chat entity with the provided message and metadata
     * 3. Saves the chat entity to the database
     * 4. Maps the saved entity to a response DTO and returns it
     * 
     * Security note: The username comes from the Authentication object, which is
     * populated by Spring Security based on the authenticated user's credentials.
     */
    @Override
    public ChatMessageResponse addChatMessage(Long teamspaceId, ChatMessageRequest chatMessageRequest, String username) {
        // Find the user by username or throw an exception if not found
        User sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create a new Chat entity using the builder pattern
        Chat chat = Chat.builder()
                .message(chatMessageRequest.getMessage())
                .timestamp(LocalDateTime.now()) // Set current timestamp
                .teamspaceId(teamspaceId)
                .sender(sender)
                .build();

        // Save the chat message to the database
        Chat savedChat = chatRepository.save(chat);
        
        // Convert the saved entity to a response DTO and return it
        return mapChatToResponse(savedChat);
    }

    /**
     * Helper method to map a Chat entity to a ChatMessageResponse DTO.
     * This encapsulates the transformation logic in one place for reuse.
     * 
     * The mapping process extracts relevant data from the Chat entity and its
     * associations (like the sender) to create a flat DTO structure suitable
     * for the API response.
     * 
     * @param chat The Chat entity to map
     * @return A ChatMessageResponse containing the chat data in API format
     */
    private ChatMessageResponse mapChatToResponse(Chat chat) {
        return ChatMessageResponse.builder()
                .id(chat.getId())
                .message(chat.getMessage())
                .timestamp(chat.getTimestamp())
                .teamspaceId(chat.getTeamspaceId())
                .senderId(Long.valueOf(chat.getSender().getUserId()))
                .senderUsername(chat.getSender().getUsername())
                .build();
    }
} 