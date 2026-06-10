package com.hei.school.service;

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
import com.hei.school.repository.AuthorRepository;
import com.hei.school.repository.BookRepository;
import com.hei.school.repository.GenreRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;
  private final AuthorRepository authorRepository;
  private final GenreRepository genreRepository;
  private final BookMapper bookMapper;

  @Override
  public Page<BookResponse> getAllBooks(
      Language language, UUID genreId, UUID authorId, String search, Pageable pageable) {

    boolean hasSearch = search != null && !search.isBlank();
    boolean hasLanguage = language != null;
    boolean hasGenre = genreId != null;
    boolean hasAuthor = authorId != null;

    String languageStr = hasLanguage ? language.name() : null;

    Page<Book> page;

    if (hasLanguage && hasSearch) {
      page = bookRepository.findByLanguageAndSearch(languageStr, search.trim(), pageable);
    } else if (hasGenre && hasSearch) {
      page = bookRepository.findByGenreIdAndSearch(genreId, search.trim(), pageable);
    } else if (hasAuthor && hasSearch) {
      page = bookRepository.findByAuthorIdAndSearch(authorId, search.trim(), pageable);
    } else if (hasSearch) {
      page = bookRepository.searchByTitleOrDescription(search.trim(), pageable);
    } else if (hasLanguage) {
      page = bookRepository.findByLanguage(languageStr, pageable);
    } else if (hasGenre) {
      page = bookRepository.findByGenreId(genreId, pageable);
    } else if (hasAuthor) {
      page = bookRepository.findByAuthorId(authorId, pageable);
    } else {
      page = bookRepository.findAll(pageable);
    }

    return page.map(bookMapper::toResponse);
  }

  @Override
  public BookResponse getBookById(UUID id) {
    return bookMapper.toResponse(findBookOrThrow(id));
  }

  @Override
  @Transactional
  public BookResponse createBook(BookCreateRequest request) {
    if (bookRepository.existsByTitleIgnoreCase(request.getTitle())) {
      throw new BookAlreadyExistsException(request.getTitle());
    }

    Book book = bookMapper.toEntity(request);
    book.setAuthors(resolveAuthors(request.getAuthorIds()));
    book.setGenres(resolveGenres(request.getGenreIds()));

    return bookMapper.toResponse(bookRepository.save(book));
  }

  @Override
  @Transactional
  public BookResponse updateBook(UUID id, BookUpdateRequest request) {
    Book book = findBookOrThrow(id);

    book.setTitle(request.getTitle());
    book.setDescription(request.getDescription());
    book.setLanguage(request.getLanguage());
    book.setAuthors(resolveAuthors(request.getAuthorIds()));
    book.setGenres(resolveGenres(request.getGenreIds()));

    return bookMapper.toResponse(bookRepository.save(book));
  }

  @Override
  @Transactional
  public BookResponse patchBook(UUID id, BookPatchRequest request) {
    Book book = findBookOrThrow(id);

    if (request.getTitle() != null) {
      book.setTitle(request.getTitle());
    }
    if (request.getDescription() != null) {
      book.setDescription(request.getDescription());
    }
    if (request.getLanguage() != null) {
      book.setLanguage(request.getLanguage());
    }
    if (request.getAuthorIds() != null && !request.getAuthorIds().isEmpty()) {
      book.setAuthors(resolveAuthors(request.getAuthorIds()));
    }
    if (request.getGenreIds() != null) {
      book.setGenres(resolveGenres(request.getGenreIds()));
    }

    return bookMapper.toResponse(bookRepository.save(book));
  }

  @Override
  @Transactional
  public void deleteBook(UUID id) {
    if (!bookRepository.existsById(id)) {
      throw new BookNotFoundException(id);
    }
    bookRepository.deleteById(id);
  }

  private Book findBookOrThrow(UUID id) {
    return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
  }

  private List<Author> resolveAuthors(List<UUID> ids) {
    if (ids == null || ids.isEmpty()) return List.of();
    return ids.stream()
        .map(
            authorId ->
                authorRepository
                    .findById(authorId)
                    .orElseThrow(
                        () ->
                            new RuntimeException("Author with id " + authorId + " was not found")))
        .toList();
  }

  private List<Genre> resolveGenres(List<UUID> ids) {
    if (ids == null || ids.isEmpty()) return List.of();
    return ids.stream()
        .map(
            genreId ->
                genreRepository
                    .findById(genreId)
                    .orElseThrow(
                        () -> new RuntimeException("Genre with id " + genreId + " was not found")))
        .toList();
  }
}
