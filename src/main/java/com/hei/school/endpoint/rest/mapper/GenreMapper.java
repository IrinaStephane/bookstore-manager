package com.hei.school.endpoint.rest.mapper;

import com.hei.school.endpoint.rest.model.GenreCreateRequest;
import com.hei.school.endpoint.rest.model.GenreResponse;
import com.hei.school.entity.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {

  public GenreResponse toResponse(Genre genre) {
    return GenreResponse.builder().id(genre.getId()).name(genre.getName()).build();
  }

  public Genre toEntity(GenreCreateRequest request) {
    Genre genre = new Genre();
    genre.setName(request.getName());
    return genre;
  }
}
