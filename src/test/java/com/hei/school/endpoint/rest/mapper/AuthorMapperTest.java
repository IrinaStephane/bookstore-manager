package com.hei.school.endpoint.rest.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.hei.school.endpoint.rest.model.AuthorCreateRequest;
import com.hei.school.endpoint.rest.model.AuthorResponse;
import com.hei.school.entity.Author;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AuthorMapperTest {

  private final AuthorMapper authorMapper = new AuthorMapper();

  @Test
  void toResponseShouldMapAllFields() {
    var id = UUID.randomUUID();
    var author = new Author(id, "John", "Doe", "A great writer");

    AuthorResponse result = authorMapper.toResponse(author);

    assertEquals(id, result.getId());
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("A great writer", result.getBio());
  }

  @Test
  void toResponseShouldMapNullBio() {
    var author = new Author(UUID.randomUUID(), "John", "Doe", null);

    AuthorResponse result = authorMapper.toResponse(author);

    assertNull(result.getBio());
  }

  @Test
  void toEntityShouldMapAllFields() {
    var request =
        AuthorCreateRequest.builder()
            .firstName("Jane")
            .lastName("Austen")
            .bio("English novelist")
            .build();

    Author result = authorMapper.toEntity(request);

    assertNull(result.getId());
    assertEquals("Jane", result.getFirstName());
    assertEquals("Austen", result.getLastName());
    assertEquals("English novelist", result.getBio());
  }

  @Test
  void toEntityShouldMapNullBio() {
    var request = AuthorCreateRequest.builder().firstName("Jane").lastName("Austen").build();

    Author result = authorMapper.toEntity(request);

    assertEquals("Jane", result.getFirstName());
    assertEquals("Austen", result.getLastName());
    assertNull(result.getBio());
  }
}
