package com.hei.school.service;

import com.hei.school.endpoint.rest.model.PublisherCreateRequest;
import com.hei.school.endpoint.rest.model.PublisherResponse;
import java.util.List;
import java.util.UUID;

public interface PublisherService {
  List<PublisherResponse> getAll();

  PublisherResponse getById(UUID id);

  PublisherResponse create(PublisherCreateRequest request);

  void delete(UUID id);
}
