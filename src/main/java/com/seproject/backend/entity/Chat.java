package com.seproject.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a chat message in the application.
 * 
 * This class is mapped to the 'chats' table in the database and stores all
 * information related to individual chat messages within teamspaces.
 * 
 * Key relationships:
 * - Each chat message belongs to exactly one teamspace (referenced by teamspaceId)
 * - Each chat message has exactly one sender (User entity)
 * 
 * The Chat entity uses a builder pattern implementation to facilitate easy object creation
 * while maintaining immutability where appropriate.
 */
@Entity
@Table(name = "chats")
public class Chat {
    /** Unique identifier for the chat message */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The actual content of the chat message */
    @Column(nullable = false)
    private String message;

    /** When the message was sent, used for chronological ordering */
    @Column(nullable = false)
    private LocalDateTime timestamp;

    /** 
     * Foreign key to teamspace
     * A teamspace acts as a container for related chat messages
     */
    @Column(name = "teamspace_id", nullable = false)
    private Long teamspaceId;

    /**
     * The user who sent this message
     * Uses eager fetching to ensure sender data is always available
     * when retrieving messages
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User sender;
    
    /**
     * Default constructor required by JPA
     */
    public Chat() {
    }
    
    /**
     * Full constructor for creating chat instances with all fields
     * 
     * @param id Unique identifier
     * @param message Chat message content
     * @param timestamp When the message was sent
     * @param teamspaceId The teamspace this message belongs to
     * @param sender The user who sent the message
     */
    public Chat(Long id, String message, LocalDateTime timestamp, Long teamspaceId, User sender) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.teamspaceId = teamspaceId;
        this.sender = sender;
    }
    
    // Standard getters and setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public Long getTeamspaceId() {
        return teamspaceId;
    }
    
    public void setTeamspaceId(Long teamspaceId) {
        this.teamspaceId = teamspaceId;
    }
    
    public User getSender() {
        return sender;
    }
    
    public void setSender(User sender) {
        this.sender = sender;
    }
    
    /**
     * Builder class for creating Chat instances in a fluent, readable manner.
     * This pattern allows for more flexible object creation and better readability
     * when constructing Chat objects.
     */
    public static class ChatBuilder {
        private Long id;
        private String message;
        private LocalDateTime timestamp;
        private Long teamspaceId;
        private User sender;
        
        public ChatBuilder() {
        }
        
        public ChatBuilder id(Long id) {
            this.id = id;
            return this;
        }
        
        public ChatBuilder message(String message) {
            this.message = message;
            return this;
        }
        
        public ChatBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public ChatBuilder teamspaceId(Long teamspaceId) {
            this.teamspaceId = teamspaceId;
            return this;
        }
        
        public ChatBuilder sender(User sender) {
            this.sender = sender;
            return this;
        }
        
        public Chat build() {
            return new Chat(id, message, timestamp, teamspaceId, sender);
        }
    }
    
    /**
     * Static factory method to create a new builder
     * 
     * @return A new ChatBuilder instance
     */
    public static ChatBuilder builder() {
        return new ChatBuilder();
    }
} 