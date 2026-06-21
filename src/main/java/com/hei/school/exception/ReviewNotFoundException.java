package com.hei.school.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReviewNotFoundException extends RuntimeException {

  public ReviewNotFoundException(UUID id) {
    super("Review with id " + id + " was not found");
  }
}
