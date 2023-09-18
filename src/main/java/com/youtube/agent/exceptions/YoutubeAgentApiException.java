package com.youtube.agent.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class YoutubeAgentApiException extends RuntimeException {
    private HttpStatus httpStatus;

    public YoutubeAgentApiException(String message) {
        super(message);
    }

    public YoutubeAgentApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
