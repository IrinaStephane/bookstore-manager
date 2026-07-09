package com.hei.school.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.hei.school.endpoint.rest.mapper.AuthorMapper;
import com.hei.school.endpoint.rest.model.AuthorCreateRequest;
import com.hei.school.endpoint.rest.model.AuthorResponse;
import com.hei.school.endpoint.rest.model.AuthorUpdateRequest;
import com.hei.school.entity.Author;
import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.repository.AuthorRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

  @Mock private AuthorRepository authorRepository;
  @Mock private AuthorMapper authorMapper;
  @InjectMocks private AuthorServiceImpl authorService;

  @Test
  void getAllAuthorsWithoutFilterShouldReturnAll() {
    var pageable = PageRequest.of(0, 20);
    var author = new Author();
    author.setId(UUID.randomUUID());
    var authors = new PageImpl<>(List.of(author));
    var response = AuthorResponse.builder().build();

    given(authorRepository.findAll(pageable)).willReturn(authors);
    given(authorMapper.toResponse(author)).willReturn(response);

    Page<AuthorResponse> result = authorService.getAllAuthors(null, pageable);

    assertEquals(1, result.getTotalElements());
    then(authorRepository).should().findAll(pageable);
  }

  @Test
  void getAllAuthorsWithNameFilterShouldSearch() {
    var pageable = PageRequest.of(0, 20);
    given(
            authorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                "Orwell", "Orwell", pageable))
        .willReturn(Page.empty());

    authorService.getAllAuthors("Orwell", pageable);

    then(authorRepository)
        .should()
        .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            "Orwell", "Orwell", pageable);
  }

  @Test
  void getByIdShouldReturnAuthor() {
    var id = UUID.randomUUID();
    var author = new Author();
    var response = AuthorResponse.builder().build();

    given(authorRepository.findById(id)).willReturn(Optional.of(author));
    given(authorMapper.toResponse(author)).willReturn(response);

    AuthorResponse result = authorService.getById(id);

    assertNotNull(result);
    then(authorRepository).should().findById(id);
  }

  @Test
  void getByIdShouldThrowWhenNotFound() {
    var id = UUID.randomUUID();
    given(authorRepository.findById(id)).willReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> authorService.getById(id));
  }

  @Test
  void createShouldPersistAndReturn() {
    var request = AuthorCreateRequest.builder().firstName("George").lastName("Orwell").build();
    var author = new Author();
    var saved = new Author();
    var response = AuthorResponse.builder().build();

    given(authorMapper.toEntity(request)).willReturn(author);
    given(authorRepository.save(author)).willReturn(saved);
    given(authorMapper.toResponse(saved)).willReturn(response);

    AuthorResponse result = authorService.create(request);

    assertNotNull(result);
    then(authorMapper).should().toEntity(request);
    then(authorRepository).should().save(author);
  }

  @Test
  void updateShouldModifyAndReturn() {
    var id = UUID.randomUUID();
    var existing = new Author();
    existing.setFirstName("George");
    existing.setLastName("Orwell");
    var request = AuthorUpdateRequest.builder().firstName("Eric").lastName("Blair").build();
    var response = AuthorResponse.builder().build();

    given(authorRepository.findById(id)).willReturn(Optional.of(existing));
    given(authorRepository.save(existing)).willReturn(existing);
    given(authorMapper.toResponse(existing)).willReturn(response);

    AuthorResponse result = authorService.update(id, request);

    assertEquals("Eric", existing.getFirstName());
    assertEquals("Blair", existing.getLastName());
    assertNotNull(result);
  }

  @Test
  void updateShouldThrowWhenNotFound() {
    var id = UUID.randomUUID();
    given(authorRepository.findById(id)).willReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> authorService.update(id, AuthorUpdateRequest.builder().build()));
  }

  @Test
  void deleteShouldRemoveWhenExists() {
    var id = UUID.randomUUID();
    given(authorRepository.existsById(id)).willReturn(true);

    authorService.delete(id);

    then(authorRepository).should().deleteById(id);
  }

  @Test
  void deleteShouldThrowWhenNotFound() {
    var id = UUID.randomUUID();
    given(authorRepository.existsById(id)).willReturn(false);

    assertThrows(ResourceNotFoundException.class, () -> authorService.delete(id));
  }
}
