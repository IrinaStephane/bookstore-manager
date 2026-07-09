package com.hei.school.endpoint.rest;

import com.hei.school.exception.BadRequestException;
import com.hei.school.exception.DuplicateResourceException;
import com.hei.school.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(
      ResourceNotFoundException e, WebRequest request) {
    log.warn("Resource not found: {}", e.getMessage());
    return buildResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
  }

  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ErrorResponse> handleConflict(
      DuplicateResourceException e, WebRequest request) {
    log.warn("Conflict: {}", e.getMessage());
    return buildResponse(HttpStatus.CONFLICT, e.getMessage(), request);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException e, WebRequest request) {
    log.warn("Bad request: {}", e.getMessage());
    return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(
      IllegalArgumentException e, WebRequest request) {
    log.warn("Invalid argument: {}", e.getMessage());
    return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpected(Exception e, WebRequest request) {
    log.error("Unexpected error", e);
    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "An unexpected error occurred. Please try again later.",
        request);
  }

  private ResponseEntity<ErrorResponse> buildResponse(
      HttpStatus status, String message, WebRequest request) {
    ErrorResponse body =
        new ErrorResponse(
            status.value(),
            status.getReasonPhrase(),
            message,
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", ""));
    return ResponseEntity.status(status).body(body);
  }
}
