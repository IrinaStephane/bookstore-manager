package com.hei.school.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {

  public BookNotFoundException(UUID id) {
    super("Book with id " + id + " was not found");
  }

  public BookNotFoundException(String message) {
    super(message);
  }
}
