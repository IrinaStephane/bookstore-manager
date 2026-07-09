package com.hei.school.endpoint.rest.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.hei.school.endpoint.rest.model.PublisherCreateRequest;
import com.hei.school.endpoint.rest.model.PublisherResponse;
import com.hei.school.entity.Publisher;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PublisherMapperTest {

  private final PublisherMapper publisherMapper = new PublisherMapper();

  @Test
  void toResponseShouldMapAllFields() {
    var id = UUID.randomUUID();
    var publisher = new Publisher(id, "Penguin Books", "UK");

    PublisherResponse result = publisherMapper.toResponse(publisher);

    assertEquals(id, result.getId());
    assertEquals("Penguin Books", result.getName());
    assertEquals("UK", result.getCountry());
  }

  @Test
  void toResponseShouldMapNullCountry() {
    var publisher = new Publisher(UUID.randomUUID(), "Penguin Books", null);

    PublisherResponse result = publisherMapper.toResponse(publisher);

    assertNull(result.getCountry());
  }

  @Test
  void toEntityShouldMapAllFields() {
    var request = new PublisherCreateRequest("Gallimard", "France");

    Publisher result = publisherMapper.toEntity(request);

    assertNull(result.getId());
    assertEquals("Gallimard", result.getName());
    assertEquals("France", result.getCountry());
  }

  @Test
  void toEntityShouldMapNullCountry() {
    var request = new PublisherCreateRequest("Gallimard", null);

    Publisher result = publisherMapper.toEntity(request);

    assertEquals("Gallimard", result.getName());
    assertNull(result.getCountry());
  }
}
