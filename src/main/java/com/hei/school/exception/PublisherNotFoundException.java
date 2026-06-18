package com.hei.school.exception;

import java.util.UUID;

public class PublisherNotFoundException extends RuntimeException {

  public PublisherNotFoundException(UUID id) {
    super("Publisher with id " + id + " was not found");
  }
}
