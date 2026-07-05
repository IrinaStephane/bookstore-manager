package com.hei.school.endpoint.rest.mapper;

import com.hei.school.PojaGenerated;
import com.hei.school.endpoint.rest.model.AuthorSummary;
import com.hei.school.endpoint.rest.model.BookCreateRequest;
import com.hei.school.endpoint.rest.model.BookEditionResponse;
import com.hei.school.endpoint.rest.model.BookResponse;
import com.hei.school.endpoint.rest.model.GenreSummary;
import com.hei.school.endpoint.rest.model.ReviewResponse;
import com.hei.school.entity.Author;
import com.hei.school.entity.Book;
import com.hei.school.entity.Genre;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@PojaGenerated
public class BookMapper {

  private final BookEditionMapper bookEditionMapper;
  private final ReviewMapper reviewMapper;

  public BookResponse toResponse(Book domain) {
    List<BookEditionResponse> editions = domain.getEditions() == null
        ? Collections.emptyList()
        : domain.getEditions().stream().map(bookEditionMapper::toResponse).toList();
    List<ReviewResponse> reviews = domain.getReviews() == null
        ? Collections.emptyList()
        : domain.getReviews().stream().map(reviewMapper::toResponse).toList();
    return BookResponse.builder()
        .id(domain.getId())
        .title(domain.getTitle())
        .description(domain.getDescription())
        .language(domain.getLanguage())
        .authors(
            domain.getAuthors().stream().map(this::toAuthorSummary).collect(Collectors.toList()))
        .genres(domain.getGenres().stream().map(this::toGenreSummary).collect(Collectors.toList()))
        .editions(editions)
        .reviews(reviews)
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
