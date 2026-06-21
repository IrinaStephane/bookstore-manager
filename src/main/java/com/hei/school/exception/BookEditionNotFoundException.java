package com.hei.school.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookEditionNotFoundException extends RuntimeException {

  public BookEditionNotFoundException(UUID id) {
    super("Book edition with id " + id + " was not found");
  }
}
