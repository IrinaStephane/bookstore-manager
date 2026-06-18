package com.hei.school.endpoint.rest.controller.Genre;

import com.hei.school.endpoint.rest.model.GenreCreateRequest;
import com.hei.school.endpoint.rest.model.GenreResponse;
import com.hei.school.service.GenreService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {

  private final GenreService genreService;

  @GetMapping
  public List<GenreResponse> getAllGenres() {
    return genreService.getAll();
  }

  @GetMapping("/{id}")
  public GenreResponse getGenreById(@PathVariable UUID id) {
    return genreService.getById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public GenreResponse createGenre(@Valid @RequestBody GenreCreateRequest request) {
    return genreService.create(request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteGenre(@PathVariable UUID id) {
    genreService.delete(id);
  }
}
