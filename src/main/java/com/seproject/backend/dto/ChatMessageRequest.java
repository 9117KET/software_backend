package com.seproject.backend.dto;

/**
 * Data Transfer Object (DTO) for receiving chat message requests from clients.
 * 
 * This class is used to transfer chat message data from the client to the server
 * without exposing the internal entity structure. It only contains the message
 * content since other metadata like the sender and timestamp will be set by the server.
 * 
 * The DTO pattern is used to:
 * 1. Decouple the API layer from the domain model
 * 2. Control exactly what data is exposed to/accepted from clients
 * 3. Allow the internal model to evolve independently of the API contract
 * 
 * This class also uses the builder pattern for easier object creation, especially
 * useful for testing and when constructing instances in service methods.
 */
public class ChatMessageRequest {
    /**
     * The content of the chat message
     */
    private String message;
    
    /**
     * Default constructor required for JSON deserialization
     */
    public ChatMessageRequest() {
    }
    
    /**
     * Constructor with message content
     * 
     * @param message The content of the chat message
     */
    public ChatMessageRequest(String message) {
        this.message = message;
    }
    
    /**
     * Gets the message content
     * 
     * @return The content of the chat message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Sets the message content
     * 
     * @param message The content to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Builder class for creating ChatMessageRequest instances in a fluent manner.
     * This inner static class implements the Builder pattern.
     */
    public static class ChatMessageRequestBuilder {
        private String message;
        
        public ChatMessageRequestBuilder() {
        }
        
        /**
         * Sets the message content and returns the builder for chaining
         * 
         * @param message The message content
         * @return This builder instance for method chaining
         */
        public ChatMessageRequestBuilder message(String message) {
            this.message = message;
            return this;
        }
        
        /**
         * Builds and returns a new ChatMessageRequest with the configured values
         * 
         * @return A new ChatMessageRequest instance
         */
        public ChatMessageRequest build() {
            return new ChatMessageRequest(message);
        }
    }
    
    /**
     * Static factory method to create a new builder
     * 
     * @return A new ChatMessageRequestBuilder instance
     */
    public static ChatMessageRequestBuilder builder() {
        return new ChatMessageRequestBuilder();
    }
} 