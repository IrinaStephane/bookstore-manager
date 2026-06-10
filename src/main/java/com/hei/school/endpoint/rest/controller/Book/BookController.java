package com.hei.school.endpoint.rest.controller;

import com.hei.school.PojaGenerated;
import com.hei.school.endpoint.rest.mapper.BookMapper;
import com.hei.school.endpoint.rest.model.*;
import com.hei.school.entity.enums.Language;
import com.hei.school.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@PojaGenerated
public class BookController {
  private final BookService service;
  private final BookMapper mapper;

  @GetMapping("/books")
  public Page<BookResponse> getAllBooks(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(required = false) Language language,
      @RequestParam(required = false) Long authorId,
      @RequestParam(required = false) Long genreId,
      @RequestParam(required = false) String search) {
    Pageable pageable = PageRequest.of(page, size);
    return service.getAllBooks(language, authorId, genreId, search, pageable).map(mapper::toRest);
  }

  @GetMapping("/books/{id}")
  public BookResponse getBookById(@PathVariable Long id) {
    return mapper.toRest(service.findById(id));
  }

  @PostMapping("/books")
  public BookResponse createBook(@Valid @RequestBody BookCreateRequest request) {
    return mapper.toRest(service.save(mapper.toDomain(request)));
  }

  @PutMapping("/books/{id}")
  public BookResponse updateBook(
      @PathVariable Long id, @Valid @RequestBody BookUpdateRequest request) {
  }

  @PatchMapping("/books/{id}")
  public BookResponse patchBook(@PathVariable Long id, @RequestBody BookPatchRequest request) {
  }

  @DeleteMapping("/books/{id}")
  public void deleteBook(@PathVariable Long id) {
  }
}
