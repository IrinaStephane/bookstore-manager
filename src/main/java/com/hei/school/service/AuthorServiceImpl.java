package com.hei.school.service;

import com.hei.school.endpoint.rest.mapper.AuthorMapper;
import com.hei.school.endpoint.rest.model.AuthorCreateRequest;
import com.hei.school.endpoint.rest.model.AuthorResponse;
import com.hei.school.endpoint.rest.model.AuthorUpdateRequest;
import com.hei.school.entity.Author;
import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.repository.AuthorRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository authorRepository;
  private final AuthorMapper authorMapper;

  @Override
  public Page<AuthorResponse> getAllAuthors(String name, Pageable pageable) {
    if (name != null && !name.isBlank()) {
      return authorRepository
          .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
              name.trim(), name.trim(), pageable)
          .map(authorMapper::toResponse);
    }
    return authorRepository.findAll(pageable).map(authorMapper::toResponse);
  }

  @Override
  public AuthorResponse getById(UUID id) {
    return authorMapper.toResponse(findOrThrow(id));
  }

  @Override
  @Transactional
  public AuthorResponse create(AuthorCreateRequest request) {
    Author author = authorMapper.toEntity(request);
    return authorMapper.toResponse(authorRepository.save(author));
  }

  @Override
  @Transactional
  public AuthorResponse update(UUID id, AuthorUpdateRequest request) {
    Author author = findOrThrow(id);
    if (request.getFirstName() != null) {
      author.setFirstName(request.getFirstName());
    }
    if (request.getLastName() != null) {
      author.setLastName(request.getLastName());
    }
    if (request.getBio() != null) {
      author.setBio(request.getBio());
    }
    return authorMapper.toResponse(authorRepository.save(author));
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    if (!authorRepository.existsById(id)) {
      throw new ResourceNotFoundException("Author with id " + id + " was not found");
    }
    authorRepository.deleteById(id);
  }

  private Author findOrThrow(UUID id) {
    return authorRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Author with id " + id + " was not found"));
  }
}
