package com.hei.school.service;

import com.hei.school.endpoint.rest.model.AuthorCreateRequest;
import com.hei.school.endpoint.rest.model.AuthorResponse;
import com.hei.school.endpoint.rest.model.AuthorUpdateRequest;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
  Page<AuthorResponse> getAllAuthors(String name, Pageable pageable);

  AuthorResponse getById(UUID id);

  AuthorResponse create(AuthorCreateRequest request);

  AuthorResponse update(UUID id, AuthorUpdateRequest request);

  void delete(UUID id);
}
