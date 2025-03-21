package com.seproject.backend.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for sending chat message responses to clients.
 * 
 * This class encapsulates all the data needed by clients to display a chat message,
 * including metadata such as the sender, timestamp, and identifiers, without
 * exposing internal entity structures directly.
 * 
 * Unlike the request DTO which is minimal, this response DTO includes comprehensive
 * information as clients need the complete context of a message to display it properly.
 * 
 * The separation between entity (Chat) and this DTO allows:
 * 1. API evolution independent of the database schema
 * 2. Control over exactly what data is exposed to clients
 * 3. Transformation and formatting of data before sending to clients
 * 
 * This DTO is used in both GET operations (retrieving messages) and as the response
 * for POST operations (confirming a message was created).
 */
public class ChatMessageResponse {
    /**
     * Unique identifier of the chat message
     */
    private Long id;
    
    /**
     * Content of the chat message
     */
    private String message;
    
    /**
     * When the message was sent
     */
    private LocalDateTime timestamp;
    
    /**
     * The ID of the teamspace this message belongs to
     */
    private Long teamspaceId;
    
    /**
     * The ID of the user who sent the message
     */
    private Long senderId;
    
    /**
     * The username of the sender for display purposes
     * This avoids additional API calls to fetch user details separately
     */
    private String senderUsername;
    
    /**
     * Default constructor required for JSON serialization
     */
    public ChatMessageResponse() {
    }
    
    /**
     * Full constructor with all fields
     * 
     * @param id Unique identifier of the message
     * @param message Content of the message
     * @param timestamp When the message was sent
     * @param teamspaceId The teamspace this message belongs to
     * @param senderId The ID of the sender
     * @param senderUsername The username of the sender
     */
    public ChatMessageResponse(Long id, String message, LocalDateTime timestamp, Long teamspaceId, Long senderId, String senderUsername) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.teamspaceId = teamspaceId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
    }
    
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
    
    public Long getSenderId() {
        return senderId;
    }
    
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
    
    public String getSenderUsername() {
        return senderUsername;
    }
    
    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
    
    /**
     * Builder class for creating ChatMessageResponse instances.
     * Implements the Builder pattern for fluent and readable object construction.
     */
    public static class ChatMessageResponseBuilder {
        private Long id;
        private String message;
        private LocalDateTime timestamp;
        private Long teamspaceId;
        private Long senderId;
        private String senderUsername;
        
        public ChatMessageResponseBuilder() {
        }
        
        /**
         * Sets the message ID
         * 
         * @param id The message ID
         * @return This builder instance
         */
        public ChatMessageResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }
        
        /**
         * Sets the message content
         * 
         * @param message The message content
         * @return This builder instance
         */
        public ChatMessageResponseBuilder message(String message) {
            this.message = message;
            return this;
        }
        
        /**
         * Sets the message timestamp
         * 
         * @param timestamp When the message was sent
         * @return This builder instance
         */
        public ChatMessageResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        /**
         * Sets the teamspace ID
         * 
         * @param teamspaceId The teamspace ID
         * @return This builder instance
         */
        public ChatMessageResponseBuilder teamspaceId(Long teamspaceId) {
            this.teamspaceId = teamspaceId;
            return this;
        }
        
        /**
         * Sets the sender ID
         * 
         * @param senderId The sender ID
         * @return This builder instance
         */
        public ChatMessageResponseBuilder senderId(Long senderId) {
            this.senderId = senderId;
            return this;
        }
        
        /**
         * Sets the sender username
         * 
         * @param senderUsername The sender username
         * @return This builder instance
         */
        public ChatMessageResponseBuilder senderUsername(String senderUsername) {
            this.senderUsername = senderUsername;
            return this;
        }
        
        /**
         * Builds a new ChatMessageResponse with the configured values
         * 
         * @return A new ChatMessageResponse instance
         */
        public ChatMessageResponse build() {
            return new ChatMessageResponse(id, message, timestamp, teamspaceId, senderId, senderUsername);
        }
    }
    
    /**
     * Static factory method to create a new builder
     * 
     * @return A new ChatMessageResponseBuilder instance
     */
    public static ChatMessageResponseBuilder builder() {
        return new ChatMessageResponseBuilder();
    }
} 