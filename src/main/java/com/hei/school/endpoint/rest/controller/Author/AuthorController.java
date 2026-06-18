package com.hei.school.endpoint.rest.controller.Author;

import com.hei.school.endpoint.rest.model.AuthorCreateRequest;
import com.hei.school.endpoint.rest.model.AuthorResponse;
import com.hei.school.endpoint.rest.model.AuthorUpdateRequest;
import com.hei.school.service.AuthorService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
@AllArgsConstructor
public class AuthorController {

  private final AuthorService authorService;

  @GetMapping
  public Page<AuthorResponse> getAllAuthors(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(required = false) String name) {
    Pageable pageable = PageRequest.of(page, size);
    return authorService.getAllAuthors(name, pageable);
  }

  @GetMapping("/{id}")
  public AuthorResponse getAuthorById(@PathVariable UUID id) {
    return authorService.getById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AuthorResponse createAuthor(@Valid @RequestBody AuthorCreateRequest request) {
    return authorService.create(request);
  }

  @PutMapping("/{id}")
  public AuthorResponse updateAuthor(
      @PathVariable UUID id, @Valid @RequestBody AuthorUpdateRequest request) {
    return authorService.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAuthor(@PathVariable UUID id) {
    authorService.delete(id);
  }
}
