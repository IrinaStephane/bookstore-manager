package com.hei.school.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PublisherNotFoundException extends RuntimeException {

  public PublisherNotFoundException(UUID id) {
    super("Publisher with id " + id + " was not found");
  }
}
