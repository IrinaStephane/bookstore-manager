package com.hei.school.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.hei.school.endpoint.rest.mapper.BookMapper;
import com.hei.school.endpoint.rest.model.BookCreateRequest;
import com.hei.school.endpoint.rest.model.BookPatchRequest;
import com.hei.school.endpoint.rest.model.BookResponse;
import com.hei.school.endpoint.rest.model.BookUpdateRequest;
import com.hei.school.entity.Author;
import com.hei.school.entity.Book;
import com.hei.school.entity.Genre;
import com.hei.school.entity.enums.Language;
import com.hei.school.exception.BookAlreadyExistsException;
import com.hei.school.exception.BookNotFoundException;
import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.repository.AuthorRepository;
import com.hei.school.repository.BookRepository;
import com.hei.school.repository.GenreRepository;
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
class BookServiceImplTest {

  @Mock private BookRepository bookRepository;
  @Mock private AuthorRepository authorRepository;
  @Mock private GenreRepository genreRepository;
  @Mock private BookMapper bookMapper;
  @InjectMocks private BookServiceImpl bookService;

  @Test
  void getAllBooksWithoutFiltersShouldReturnAll() {
    var pageable = PageRequest.of(0, 20);
    var book = new Book();
    var books = new PageImpl<>(List.of(book));

    given(bookRepository.findAll(pageable)).willReturn(books);
    given(bookMapper.toResponse(book)).willReturn(BookResponse.builder().build());

    Page<BookResponse> result = bookService.getAllBooks(null, null, null, null, pageable);

    assertEquals(1, result.getTotalElements());
  }

  @Test
  void getBookByIdShouldReturn() {
    var id = UUID.randomUUID();
    var book = new Book();
    var response = BookResponse.builder().build();

    given(bookRepository.findById(id)).willReturn(Optional.of(book));
    given(bookMapper.toResponse(book)).willReturn(response);

    BookResponse result = bookService.getBookById(id);

    assertNotNull(result);
  }

  @Test
  void getBookByIdShouldThrowWhenNotFound() {
    given(bookRepository.findById(any())).willReturn(Optional.empty());

    assertThrows(BookNotFoundException.class, () -> bookService.getBookById(UUID.randomUUID()));
  }

  @Test
  void createBookShouldPersistAndReturn() {
    var authorId = UUID.randomUUID();
    var genreId = UUID.randomUUID();
    var request =
        BookCreateRequest.builder()
            .title("1984")
            .language(Language.EN)
            .authorIds(List.of(authorId))
            .genreIds(List.of(genreId))
            .build();
    var book = new Book();
    var saved = new Book();
    var author = new Author();
    var genre = new Genre();
    var response = BookResponse.builder().build();

    given(bookRepository.existsByTitleIgnoreCase("1984")).willReturn(false);
    given(bookMapper.toEntity(request)).willReturn(book);
    given(authorRepository.findById(authorId)).willReturn(Optional.of(author));
    given(genreRepository.findById(genreId)).willReturn(Optional.of(genre));
    given(bookRepository.save(book)).willReturn(saved);
    given(bookMapper.toResponse(saved)).willReturn(response);

    BookResponse result = bookService.createBook(request);

    assertNotNull(result);
    then(bookRepository).should().save(book);
  }

  @Test
  void createBookShouldThrowWhenDuplicateTitle() {
    var request =
        BookCreateRequest.builder()
            .title("1984")
            .language(Language.EN)
            .authorIds(List.of(UUID.randomUUID()))
            .build();

    given(bookRepository.existsByTitleIgnoreCase("1984")).willReturn(true);

    assertThrows(BookAlreadyExistsException.class, () -> bookService.createBook(request));
  }

  @Test
  void createBookShouldThrowWhenAuthorNotFound() {
    var authorId = UUID.randomUUID();
    var request =
        BookCreateRequest.builder()
            .title("1984")
            .language(Language.EN)
            .authorIds(List.of(authorId))
            .build();

    given(bookRepository.existsByTitleIgnoreCase("1984")).willReturn(false);
    given(bookMapper.toEntity(request)).willReturn(new Book());
    given(authorRepository.findById(authorId)).willReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> bookService.createBook(request));
  }

  @Test
  void updateBookShouldModifyAndReturn() {
    var id = UUID.randomUUID();
    var existing = new Book();
    existing.setTitle("Old Title");
    var request = new BookUpdateRequest();
    request.setTitle("New Title");
    request.setLanguage(Language.EN);
    request.setAuthorIds(List.of());

    given(bookRepository.findById(id)).willReturn(Optional.of(existing));
    given(bookRepository.existsByTitleIgnoreCase("New Title")).willReturn(false);
    given(bookRepository.save(existing)).willReturn(existing);
    given(bookMapper.toResponse(existing)).willReturn(BookResponse.builder().build());

    BookResponse result = bookService.updateBook(id, request);

    assertEquals("New Title", existing.getTitle());
    assertNotNull(result);
  }

  @Test
  void updateBookShouldThrowWhenDuplicateTitle() {
    var id = UUID.randomUUID();
    var existing = new Book();
    existing.setTitle("Old Title");
    var request = new BookUpdateRequest();
    request.setTitle("Taken Title");
    request.setLanguage(Language.EN);
    request.setAuthorIds(List.of());

    given(bookRepository.findById(id)).willReturn(Optional.of(existing));
    given(bookRepository.existsByTitleIgnoreCase("Taken Title")).willReturn(true);

    assertThrows(BookAlreadyExistsException.class, () -> bookService.updateBook(id, request));
  }

  @Test
  void patchBookShouldUpdatePartialFields() {
    var id = UUID.randomUUID();
    var existing = new Book();
    existing.setTitle("Old Title");
    existing.setDescription("Old desc");
    var request = new BookPatchRequest();
    request.setTitle("New Title");

    given(bookRepository.findById(id)).willReturn(Optional.of(existing));
    given(bookRepository.save(existing)).willReturn(existing);
    given(bookMapper.toResponse(existing)).willReturn(BookResponse.builder().build());

    bookService.patchBook(id, request);

    assertEquals("New Title", existing.getTitle());
    assertEquals("Old desc", existing.getDescription());
  }

  @Test
  void deleteBookShouldRemoveWhenExists() {
    var id = UUID.randomUUID();
    given(bookRepository.existsById(id)).willReturn(true);

    bookService.deleteBook(id);

    then(bookRepository).should().deleteById(id);
  }

  @Test
  void deleteBookShouldThrowWhenNotFound() {
    var id = UUID.randomUUID();
    given(bookRepository.existsById(id)).willReturn(false);

    assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(id));
  }
}
