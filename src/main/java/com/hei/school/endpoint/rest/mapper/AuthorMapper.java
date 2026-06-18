package com.hei.school.endpoint.rest.mapper;

import com.hei.school.endpoint.rest.model.AuthorCreateRequest;
import com.hei.school.endpoint.rest.model.AuthorResponse;
import com.hei.school.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

  public AuthorResponse toResponse(Author author) {
    return AuthorResponse.builder()
        .id(author.getId())
        .firstName(author.getFirstName())
        .lastName(author.getLastName())
        .bio(author.getBio())
        .build();
  }

  public Author toEntity(AuthorCreateRequest request) {
    Author author = new Author();
    author.setFirstName(request.getFirstName());
    author.setLastName(request.getLastName());
    author.setBio(request.getBio());
    return author;
  }
}
