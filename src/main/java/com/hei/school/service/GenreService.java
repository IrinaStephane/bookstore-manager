package com.hei.school.service;

import com.hei.school.endpoint.rest.model.GenreCreateRequest;
import com.hei.school.endpoint.rest.model.GenreResponse;
import java.util.List;
import java.util.UUID;

public interface GenreService {
  List<GenreResponse> getAll();

  GenreResponse getById(UUID id);

  GenreResponse create(GenreCreateRequest request);

  void delete(UUID id);
}
