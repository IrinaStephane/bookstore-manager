package com.hei.school.exception;

import java.util.UUID;

public class BookEditionNotFoundException extends RuntimeException {

  public BookEditionNotFoundException(UUID id) {
    super("Book edition with id " + id + " was not found");
  }
}
