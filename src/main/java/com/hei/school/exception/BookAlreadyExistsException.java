package com.hei.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookAlreadyExistsException extends RuntimeException {

  public BookAlreadyExistsException(String title) {
    super("A book with the title \"" + title + "\" already exists");
  }
}
