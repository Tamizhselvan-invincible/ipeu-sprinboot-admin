package com.hetero.utils;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private T data;

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

    // Getters and Setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}

