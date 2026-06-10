package com.hei.school.service;

import com.hei.school.endpoint.rest.model.BookCreateRequest;
import com.hei.school.endpoint.rest.model.BookPatchRequest;
import com.hei.school.endpoint.rest.model.BookResponse;
import com.hei.school.endpoint.rest.model.BookUpdateRequest;
import com.hei.school.entity.enums.Language;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

  Page<BookResponse> getAllBooks(
      Language language, UUID genreId, UUID authorId, String search, Pageable pageable);

  BookResponse getBookById(UUID id);

  BookResponse createBook(BookCreateRequest request);

  BookResponse updateBook(UUID id, BookUpdateRequest request);

  BookResponse patchBook(UUID id, BookPatchRequest request);

  void deleteBook(UUID id);
}
