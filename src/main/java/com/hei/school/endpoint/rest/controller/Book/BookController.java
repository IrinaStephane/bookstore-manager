package com.hei.school.endpoint.rest.controller.Book;

import com.hei.school.PojaGenerated;
import com.hei.school.endpoint.rest.model.BookCreateRequest;
import com.hei.school.endpoint.rest.model.BookPatchRequest;
import com.hei.school.endpoint.rest.model.BookResponse;
import com.hei.school.endpoint.rest.model.BookUpdateRequest;
import com.hei.school.entity.enums.Language;
import com.hei.school.service.BookService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@PojaGenerated
public class BookController {

  private final BookService bookService;

  @GetMapping("/books")
  public Page<BookResponse> getAllBooks(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(required = false) Language language,
      @RequestParam(required = false) UUID genreId,
      @RequestParam(required = false) UUID authorId,
      @RequestParam(required = false) String search) {
    Pageable pageable = PageRequest.of(page, size);
    return bookService.getAllBooks(language, genreId, authorId, search, pageable);
  }

  @GetMapping("/books/{id}")
  public BookResponse getBookById(@PathVariable UUID id) {
    return bookService.getBookById(id);
  }

  @PostMapping("/books")
  public BookResponse createBook(@Valid @RequestBody BookCreateRequest request) {
    return bookService.createBook(request);
  }

  @PutMapping("/books/{id}")
  public BookResponse updateBook(
      @PathVariable UUID id, @Valid @RequestBody BookUpdateRequest request) {
    return bookService.updateBook(id, request);
  }

  @PatchMapping("/books/{id}")
  public BookResponse patchBook(@PathVariable UUID id, @RequestBody BookPatchRequest request) {
    return bookService.patchBook(id, request);
  }

  @DeleteMapping("/books/{id}")
  public void deleteBook(@PathVariable UUID id) {
    bookService.deleteBook(id);
  }
}
