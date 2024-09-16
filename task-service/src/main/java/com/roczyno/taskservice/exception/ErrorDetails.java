package com.roczyno.taskservice.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorDetails {
    private String error;
    private String details;
    private LocalDateTime timeStamp;

    public ErrorDetails(String error, String details, LocalDateTime timeStamp) {
        super();
        this.error = error;
        this.details = details;
        this.timeStamp = timeStamp;
    }
}
