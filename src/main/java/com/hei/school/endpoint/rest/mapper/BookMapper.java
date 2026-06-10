package com.hei.school.endpoint.rest.mapper;

import com.hei.school.PojaGenerated;
import com.hei.school.endpoint.rest.model.AuthorSummary;
import com.hei.school.endpoint.rest.model.BookCreateRequest;
import com.hei.school.endpoint.rest.model.BookResponse;
import com.hei.school.endpoint.rest.model.GenreSummary;
import com.hei.school.entity.Author;
import com.hei.school.entity.Book;
import com.hei.school.entity.Genre;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
@PojaGenerated
public class BookMapper {

  public BookResponse toResponse(Book domain) {
    return BookResponse.builder()
        .id(domain.getId())
        .title(domain.getTitle())
        .description(domain.getDescription())
        .language(domain.getLanguage())
        .authors(
            domain.getAuthors().stream().map(this::toAuthorSummary).collect(Collectors.toList()))
        .genres(domain.getGenres().stream().map(this::toGenreSummary).collect(Collectors.toList()))
        .build();
  }

  public Book toEntity(BookCreateRequest request) {
    Book book = new Book();
    book.setTitle(request.getTitle());
    book.setDescription(request.getDescription());
    book.setLanguage(request.getLanguage());
    return book;
  }

  private AuthorSummary toAuthorSummary(Author author) {
    return AuthorSummary.builder()
        .id(author.getId())
        .firstName(author.getFirstName())
        .lastName(author.getLastName())
        .build();
  }

  private GenreSummary toGenreSummary(Genre genre) {
    return GenreSummary.builder().id(genre.getId()).name(genre.getName()).build();
  }
}
