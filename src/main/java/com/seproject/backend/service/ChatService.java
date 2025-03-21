package com.seproject.backend.service;

import com.seproject.backend.dto.ChatMessageRequest;
import com.seproject.backend.dto.ChatMessageResponse;

import java.util.List;

/**
 * Service interface defining the chat operations for the application.
 * 
 * This interface defines the contract for chat-related business operations,
 * following the Service layer pattern in a multi-tier architecture. It acts as
 * an intermediary between the Controller layer (API endpoints) and the Repository
 * layer (data access).
 * 
 * Benefits of this interface-based design:
 * 1. Decouples the service contract from its implementation
 * 2. Enables dependency injection and easier testing with mock implementations
 * 3. Clearly defines the operations available for chat functionality
 * 4. Follows the Interface Segregation Principle (ISP) by focusing only on chat operations
 * 
 * The service layer handles business logic such as:
 * - Validation and business rules
 * - Transactions
 * - Coordination between multiple repositories
 * - Data transformation between entities and DTOs
 */
public interface ChatService {
    
    /**
     * Retrieves all chat messages for a specific teamspace.
     * 
     * This method fetches chat messages from the repository and converts them
     * to DTOs suitable for the client API layer. Messages are ordered by timestamp
     * to present them chronologically.
     * 
     * @param teamspaceId The ID of the teamspace to retrieve chat messages for
     * @return A list of chat message responses, ordered by timestamp
     */
    List<ChatMessageResponse> getTeamspaceChat(Long teamspaceId);
    
    /**
     * Adds a new chat message to a teamspace.
     * 
     * This method:
     * 1. Resolves the sender by username
     * 2. Creates a new Chat entity
     * 3. Sets the current timestamp
     * 4. Associates the message with the teamspace
     * 5. Persists the message to the database
     * 6. Returns a DTO representation of the saved message
     * 
     * @param teamspaceId The ID of the teamspace to add the message to
     * @param chatMessageRequest Contains the message content
     * @param username The username of the authenticated sender
     * @return A response DTO containing the saved message with metadata
     * @throws RuntimeException if the user is not found
     */
    ChatMessageResponse addChatMessage(Long teamspaceId, ChatMessageRequest chatMessageRequest, String username);
} 