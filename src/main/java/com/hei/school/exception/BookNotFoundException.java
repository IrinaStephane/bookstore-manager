package com.hei.school.exception;

public class BookNotFoundException extends RuntimeException {

  public BookNotFoundException(Long id) {
    super("Book with id " + id + " was not found");
  }

  public BookNotFoundException(String message) {
    super(message);
  }
}
