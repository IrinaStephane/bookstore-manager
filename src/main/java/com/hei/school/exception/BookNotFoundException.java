package com.hei.school.exception;

import java.util.UUID;

public class BookNotFoundException extends RuntimeException {

  public BookNotFoundException(UUID id) {
    super("Book with id " + id + " was not found");
  }

  public BookNotFoundException(String message) {
    super(message);
  }
}
