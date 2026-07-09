package com.hei.school.endpoint.rest.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.hei.school.endpoint.rest.model.GenreCreateRequest;
import com.hei.school.endpoint.rest.model.GenreResponse;
import com.hei.school.entity.Genre;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class GenreMapperTest {

  private final GenreMapper genreMapper = new GenreMapper();

  @Test
  void toResponseShouldMapAllFields() {
    var id = UUID.randomUUID();
    var genre = new Genre(id, "Fiction");

    GenreResponse result = genreMapper.toResponse(genre);

    assertEquals(id, result.getId());
    assertEquals("Fiction", result.getName());
  }

  @Test
  void toEntityShouldMapAllFields() {
    var request = new GenreCreateRequest("Science Fiction");

    Genre result = genreMapper.toEntity(request);

    assertNull(result.getId());
    assertEquals("Science Fiction", result.getName());
  }
}
