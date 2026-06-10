package com.hei.school.exception;

public class BookAlreadyExistsException extends RuntimeException {

  public BookAlreadyExistsException(String title) {
    super("A book with the title \"" + title + "\" already exists");
  }
}