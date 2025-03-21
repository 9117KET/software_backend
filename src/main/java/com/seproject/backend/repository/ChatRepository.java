package com.seproject.backend.repository;

import com.seproject.backend.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Chat entity that provides database operations.
 * 
 * This interface extends Spring Data JPA's JpaRepository, which automatically
 * provides basic CRUD operations (create, read, update, delete) for the Chat entity.
 * 
 * The @Repository annotation marks this as a Spring repository bean, which will
 * be automatically discovered during component scanning and dependency injection.
 * 
 * This interface defines custom query methods beyond the basic CRUD operations
 * that are specific to the chat functionality of the application.
 */
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    
    /**
     * Retrieves all chat messages for a specific teamspace ordered by timestamp in ascending order.
     * 
     * This method leverages Spring Data JPA's method name query creation feature.
     * The method name is parsed and a query is generated automatically based on
     * the method name pattern: findBy[Property]OrderBy[Property][Direction]
     * 
     * Implementation note: The resulting query will be equivalent to:
     * SELECT * FROM chats WHERE teamspace_id = ? ORDER BY timestamp ASC
     * 
     * @param teamspaceId The ID of the teamspace to retrieve chat messages for
     * @return A list of chat messages belonging to the specified teamspace, ordered by timestamp
     */
    List<Chat> findByTeamspaceIdOrderByTimestampAsc(Long teamspaceId);
} 