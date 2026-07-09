package com.hei.school.endpoint.rest.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.hei.school.endpoint.rest.model.*;
import com.hei.school.entity.*;
import com.hei.school.entity.enums.BookFormat;
import com.hei.school.entity.enums.Language;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookMapperTest {

  private final BookEditionMapper bookEditionMapper = new BookEditionMapper();
  private final ReviewMapper reviewMapper = new ReviewMapper();
  private final BookMapper bookMapper = new BookMapper(bookEditionMapper, reviewMapper);

  private Author author;
  private Genre genre;
  private BookEdition edition;
  private Review review;
  private Book book;

  @BeforeEach
  void setUp() {
    author = new Author();
    author.setId(UUID.randomUUID());
    author.setFirstName("George");
    author.setLastName("Orwell");

    genre = new Genre();
    genre.setId(UUID.randomUUID());
    genre.setName("Dystopian");

    var publisher = new Publisher();
    publisher.setId(UUID.randomUUID());
    publisher.setName("Secker & Warburg");

    edition =
        BookEdition.builder()
            .id(UUID.randomUUID())
            .isbn("978-0451524935")
            .format(BookFormat.PAPERBACK)
            .sellingPrice(10.99)
            .publishedDate(LocalDate.of(1949, 6, 8))
            .quantityInStock(100)
            .totalReceived(100)
            .totalSold(45)
            .publisher(publisher)
            .build();

    review = new Review();
    review.setId(UUID.randomUUID());
    review.setRating(5);
    review.setComment("Classic");
    review.setUserId(UUID.randomUUID());

    book = new Book();
    book.setId(UUID.randomUUID());
    book.setTitle("1984");
    book.setDescription("A dystopian novel");
    book.setLanguage(Language.EN);
    book.setAuthors(List.of(author));
    book.setGenres(List.of(genre));
    book.setEditions(List.of(edition));
    book.setReviews(List.of(review));
  }

  @Test
  void toResponseShouldMapAllFields() {
    BookResponse result = bookMapper.toResponse(book);

    assertEquals(book.getId(), result.getId());
    assertEquals("1984", result.getTitle());
    assertEquals("A dystopian novel", result.getDescription());
    assertEquals(Language.EN, result.getLanguage());

    assertNotNull(result.getAuthors());
    assertEquals(1, result.getAuthors().size());
    assertEquals(author.getId(), result.getAuthors().get(0).getId());
    assertEquals("George", result.getAuthors().get(0).getFirstName());
    assertEquals("Orwell", result.getAuthors().get(0).getLastName());

    assertNotNull(result.getGenres());
    assertEquals(1, result.getGenres().size());
    assertEquals(genre.getId(), result.getGenres().get(0).getId());
    assertEquals("Dystopian", result.getGenres().get(0).getName());

    assertNotNull(result.getEditions());
    assertEquals(1, result.getEditions().size());
    assertEquals(edition.getId(), result.getEditions().get(0).getId());

    assertNotNull(result.getReviews());
    assertEquals(1, result.getReviews().size());
    assertEquals(review.getId(), result.getReviews().get(0).getId());
  }

  @Test
  void toResponseShouldMapNullCollectionsToEmptyLists() {
    book.setAuthors(null);
    book.setGenres(null);
    book.setEditions(null);
    book.setReviews(null);

    BookResponse result = bookMapper.toResponse(book);

    assertTrue(result.getAuthors().isEmpty());
    assertTrue(result.getGenres().isEmpty());
    assertTrue(result.getEditions().isEmpty());
    assertTrue(result.getReviews().isEmpty());
  }

  @Test
  void toEntityShouldMapAllFields() {
    var request =
        BookCreateRequest.builder()
            .title("Animal Farm")
            .description("A satirical allegory")
            .language(Language.EN)
            .authorIds(List.of(UUID.randomUUID()))
            .build();

    Book result = bookMapper.toEntity(request);

    assertNull(result.getId());
    assertEquals("Animal Farm", result.getTitle());
    assertEquals("A satirical allegory", result.getDescription());
    assertEquals(Language.EN, result.getLanguage());
  }

  @Test
  void toEntityShouldMapNullDescription() {
    var request =
        BookCreateRequest.builder()
            .title("Animal Farm")
            .language(Language.EN)
            .authorIds(List.of(UUID.randomUUID()))
            .build();

    Book result = bookMapper.toEntity(request);

    assertEquals("Animal Farm", result.getTitle());
    assertNull(result.getDescription());
  }
}
