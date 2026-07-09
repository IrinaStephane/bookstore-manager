package com.hei.school.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.hei.school.endpoint.rest.mapper.GenreMapper;
import com.hei.school.endpoint.rest.model.GenreCreateRequest;
import com.hei.school.endpoint.rest.model.GenreResponse;
import com.hei.school.entity.Genre;
import com.hei.school.exception.DuplicateResourceException;
import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.repository.GenreRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {

  @Mock private GenreRepository genreRepository;
  @Mock private GenreMapper genreMapper;
  @InjectMocks private GenreServiceImpl genreService;

  @Test
  void getAllShouldReturnAll() {
    var genre = new Genre();
    var response = GenreResponse.builder().build();

    given(genreRepository.findAll()).willReturn(List.of(genre));
    given(genreMapper.toResponse(genre)).willReturn(response);

    List<GenreResponse> result = genreService.getAll();

    assertEquals(1, result.size());
  }

  @Test
  void getAllShouldReturnEmptyWhenNone() {
    given(genreRepository.findAll()).willReturn(List.of());

    List<GenreResponse> result = genreService.getAll();

    assertTrue(result.isEmpty());
  }

  @Test
  void getByIdShouldReturn() {
    var id = UUID.randomUUID();
    var genre = new Genre();
    var response = GenreResponse.builder().build();

    given(genreRepository.findById(id)).willReturn(Optional.of(genre));
    given(genreMapper.toResponse(genre)).willReturn(response);

    GenreResponse result = genreService.getById(id);

    assertNotNull(result);
  }

  @Test
  void getByIdShouldThrowWhenNotFound() {
    given(genreRepository.findById(UUID.randomUUID())).willReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> genreService.getById(UUID.randomUUID()));
  }

  @Test
  void createShouldPersistWhenNameNotTaken() {
    var request = GenreCreateRequest.builder().name("Fiction").build();
    var genre = new Genre();
    var saved = new Genre();
    var response = GenreResponse.builder().build();

    given(genreRepository.existsByNameIgnoreCase("Fiction")).willReturn(false);
    given(genreMapper.toEntity(request)).willReturn(genre);
    given(genreRepository.save(genre)).willReturn(saved);
    given(genreMapper.toResponse(saved)).willReturn(response);

    GenreResponse result = genreService.create(request);

    assertNotNull(result);
    then(genreRepository).should().existsByNameIgnoreCase("Fiction");
  }

  @Test
  void createShouldThrowWhenDuplicateName() {
    var request = GenreCreateRequest.builder().name("Fiction").build();

    given(genreRepository.existsByNameIgnoreCase("Fiction")).willReturn(true);

    assertThrows(DuplicateResourceException.class, () -> genreService.create(request));
  }

  @Test
  void deleteShouldRemoveWhenExists() {
    var id = UUID.randomUUID();
    given(genreRepository.existsById(id)).willReturn(true);

    genreService.delete(id);

    then(genreRepository).should().deleteById(id);
  }

  @Test
  void deleteShouldThrowWhenNotFound() {
    var id = UUID.randomUUID();
    given(genreRepository.existsById(id)).willReturn(false);

    assertThrows(ResourceNotFoundException.class, () -> genreService.delete(id));
  }
}
