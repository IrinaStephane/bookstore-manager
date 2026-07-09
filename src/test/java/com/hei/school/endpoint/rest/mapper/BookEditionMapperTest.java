package com.hei.school.endpoint.rest.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.hei.school.endpoint.rest.model.BookEditionCreateRequest;
import com.hei.school.endpoint.rest.model.BookEditionResponse;
import com.hei.school.entity.BookEdition;
import com.hei.school.entity.Publisher;
import com.hei.school.entity.enums.BookCondition;
import com.hei.school.entity.enums.BookFormat;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BookEditionMapperTest {

  private final BookEditionMapper bookEditionMapper = new BookEditionMapper();

  @Test
  void toResponseShouldMapAllFields() {
    var id = UUID.randomUUID();
    var publisherId = UUID.randomUUID();
    var publisher = new Publisher(publisherId, "Penguin", "UK");
    var publishedDate = LocalDate.of(2024, 1, 15);
    var edition =
        BookEdition.builder()
            .id(id)
            .isbn("978-0141439518")
            .format(BookFormat.PAPERBACK)
            .sellingPrice(12.99)
            .publishedDate(publishedDate)
            .coverImageUrl("http://example.com/cover.jpg")
            .condition(BookCondition.NEW)
            .quantityInStock(50)
            .totalReceived(100)
            .totalSold(30)
            .publisher(publisher)
            .build();

    BookEditionResponse result = bookEditionMapper.toResponse(edition);

    assertEquals(id, result.getId());
    assertEquals("978-0141439518", result.getIsbn());
    assertEquals(BookFormat.PAPERBACK, result.getFormat());
    assertEquals(12.99, result.getSellingPrice());
    assertEquals(publishedDate, result.getPublishedDate());
    assertEquals("http://example.com/cover.jpg", result.getCoverImageUrl());
    assertEquals(BookCondition.NEW, result.getCondition());
    assertEquals(50, result.getQuantityInStock());
    assertEquals(100, result.getTotalReceived());
    assertEquals(30, result.getTotalSold());
    assertNotNull(result.getPublisher());
    assertEquals(publisherId, result.getPublisher().getId());
    assertEquals("Penguin", result.getPublisher().getName());
    assertEquals("UK", result.getPublisher().getCountry());
  }

  @Test
  void toResponseShouldMapNullPublisher() {
    var edition =
        BookEdition.builder()
            .id(UUID.randomUUID())
            .isbn("978-0141439518")
            .format(BookFormat.HARDCOVER)
            .sellingPrice(20.0)
            .quantityInStock(10)
            .totalReceived(10)
            .totalSold(0)
            .publisher(null)
            .build();

    BookEditionResponse result = bookEditionMapper.toResponse(edition);

    assertNull(result.getPublisher());
  }

  @Test
  void toEntityShouldMapAllFields() {
    var publishedDate = LocalDate.of(2024, 6, 1);
    var request =
        BookEditionCreateRequest.builder()
            .isbn("978-0141439518")
            .format(BookFormat.PAPERBACK)
            .sellingPrice(15.99)
            .publishedDate(publishedDate)
            .coverImageUrl("http://example.com/cover.jpg")
            .condition(BookCondition.NEW)
            .quantityInStock(20)
            .build();

    BookEdition result = bookEditionMapper.toEntity(request);

    assertNull(result.getId());
    assertEquals("978-0141439518", result.getIsbn());
    assertEquals(BookFormat.PAPERBACK, result.getFormat());
    assertEquals(15.99, result.getSellingPrice());
    assertEquals(publishedDate, result.getPublishedDate());
    assertEquals("http://example.com/cover.jpg", result.getCoverImageUrl());
    assertEquals(BookCondition.NEW, result.getCondition());
    assertEquals(20, result.getQuantityInStock());
    assertNull(result.getPublisher());
    assertNull(result.getBook());
  }

  @Test
  void toEntityShouldDefaultQuantityInStockToZero() {
    var request =
        BookEditionCreateRequest.builder()
            .isbn("978-0141439518")
            .format(BookFormat.DIGITAL)
            .sellingPrice(9.99)
            .quantityInStock(null)
            .build();

    BookEdition result = bookEditionMapper.toEntity(request);

    assertEquals(0, result.getQuantityInStock());
  }
}
