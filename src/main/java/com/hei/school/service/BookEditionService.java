package com.hei.school.service;

import com.hei.school.endpoint.rest.model.BookEditionCreateRequest;
import com.hei.school.endpoint.rest.model.BookEditionResponse;
import java.util.List;
import java.util.UUID;

public interface BookEditionService {

  List<BookEditionResponse> getEditionsByBookId(UUID bookId);

  BookEditionResponse addEdition(UUID bookId, BookEditionCreateRequest request);

  BookEditionResponse updateEdition(UUID bookId, UUID editionId, BookEditionCreateRequest request);

  void deleteEdition(UUID bookId, UUID editionId);
}
