package com.hei.school.service;

import com.hei.school.endpoint.rest.mapper.GenreMapper;
import com.hei.school.endpoint.rest.model.GenreCreateRequest;
import com.hei.school.endpoint.rest.model.GenreResponse;
import com.hei.school.entity.Genre;
import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.repository.GenreRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

  private final GenreRepository genreRepository;
  private final GenreMapper genreMapper;

  @Override
  public List<GenreResponse> getAll() {
    return genreRepository.findAll().stream().map(genreMapper::toResponse).toList();
  }

  @Override
  public GenreResponse getById(UUID id) {
    return genreMapper.toResponse(findOrThrow(id));
  }

  @Override
  @Transactional
  public GenreResponse create(GenreCreateRequest request) {
    if (genreRepository.existsByNameIgnoreCase(request.getName())) {
      throw new IllegalArgumentException(
          "Genre with name \"" + request.getName() + "\" already exists");
    }
    Genre genre = genreMapper.toEntity(request);
    return genreMapper.toResponse(genreRepository.save(genre));
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    if (!genreRepository.existsById(id)) {
      throw new ResourceNotFoundException("Genre with id " + id + " was not found");
    }
    genreRepository.deleteById(id);
  }

  private Genre findOrThrow(UUID id) {
    return genreRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Genre with id " + id + " was not found"));
  }
}
