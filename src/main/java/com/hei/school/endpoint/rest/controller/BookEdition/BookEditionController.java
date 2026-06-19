package com.hei.school.endpoint.rest.controller.BookEdition;

import com.hei.school.PojaGenerated;
import com.hei.school.endpoint.rest.model.BookEditionCreateRequest;
import com.hei.school.endpoint.rest.model.BookEditionResponse;
import com.hei.school.service.BookEditionService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books/{bookId}/editions")
@AllArgsConstructor
@PojaGenerated
public class BookEditionController {

  private final BookEditionService bookEditionService;

  @GetMapping
  public List<BookEditionResponse> getEditions(@PathVariable UUID bookId) {
    return bookEditionService.getEditionsByBookId(bookId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BookEditionResponse addEdition(
      @PathVariable UUID bookId, @Valid @RequestBody BookEditionCreateRequest request) {
    return bookEditionService.addEdition(bookId, request);
  }

  @PutMapping("/{id}")
  public BookEditionResponse updateEdition(
      @PathVariable UUID bookId,
      @PathVariable UUID id,
      @Valid @RequestBody BookEditionCreateRequest request) {
    return bookEditionService.updateEdition(bookId, id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteEdition(@PathVariable UUID bookId, @PathVariable UUID id) {
    bookEditionService.deleteEdition(bookId, id);
  }
}
