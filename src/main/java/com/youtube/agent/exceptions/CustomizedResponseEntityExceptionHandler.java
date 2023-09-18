package com.youtube.agent.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Optional;


@RestControllerAdvice
@Slf4j
public class CustomizedResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handle(Exception ex, WebRequest request) {
        log.error(ex != null ? ex.getMessage() : "Exception is null", ex);
        if (ex instanceof YoutubeAgentApiException) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
                    request.getDescription(false), ((YoutubeAgentApiException) ex).getHttpStatus() == null ? HttpStatus.NOT_ACCEPTABLE.getReasonPhrase() : ((YoutubeAgentApiException) ex).getHttpStatus().getReasonPhrase());
            return new ResponseEntity<>(exceptionResponse, ((YoutubeAgentApiException) ex).getHttpStatus() == null ? HttpStatus.NOT_ACCEPTABLE : ((YoutubeAgentApiException) ex).getHttpStatus());
        } else {
            ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Unexpected Error",
                    request.getDescription(false), HttpStatus.NOT_ACCEPTABLE.getReasonPhrase());
            return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        ExceptionResponse response;
        Optional<ObjectError> objectError = ex.getBindingResult().getAllErrors().stream().findFirst();
        response = objectError.map(error -> new ExceptionResponse(new Date(), error.getDefaultMessage(), "", ""))
                .orElseGet(() -> new ExceptionResponse(new Date(), "400 Bad Request", "", ""));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
