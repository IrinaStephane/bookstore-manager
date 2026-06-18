package com.hei.school.endpoint.rest.mapper;

import com.hei.school.endpoint.rest.model.PublisherCreateRequest;
import com.hei.school.endpoint.rest.model.PublisherResponse;
import com.hei.school.entity.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {

  public PublisherResponse toResponse(Publisher publisher) {
    return PublisherResponse.builder()
        .id(publisher.getId())
        .name(publisher.getName())
        .country(publisher.getCountry())
        .build();
  }

  public Publisher toEntity(PublisherCreateRequest request) {
    Publisher publisher = new Publisher();
    publisher.setName(request.getName());
    publisher.setCountry(request.getCountry());
    return publisher;
  }
}
