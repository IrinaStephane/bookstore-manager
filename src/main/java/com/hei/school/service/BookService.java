package com.hei.school.service;

import com.hei.school.endpoint.rest.model.BookCreateRequest;
import com.hei.school.endpoint.rest.model.BookPatchRequest;
import com.hei.school.endpoint.rest.model.BookResponse;
import com.hei.school.endpoint.rest.model.BookUpdateRequest;
import com.hei.school.entity.enums.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

  /** GET /books — liste paginée avec filtres optionnels */
  Page<BookResponse> getAllBooks(
      Language language, Long genreId, Long authorId, String search, Pageable pageable);

  BookResponse getBookById(Long id);

  BookResponse createBook(BookCreateRequest request);

  BookResponse updateBook(Long id, BookUpdateRequest request);

  BookResponse patchBook(Long id, BookPatchRequest request);

  void deleteBook(Long id);
}
