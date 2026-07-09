package com.hei.school.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.hei.school.endpoint.rest.mapper.BookEditionMapper;
import com.hei.school.endpoint.rest.model.BookEditionCreateRequest;
import com.hei.school.endpoint.rest.model.BookEditionResponse;
import com.hei.school.entity.Book;
import com.hei.school.entity.BookEdition;
import com.hei.school.entity.PriceHistory;
import com.hei.school.entity.Publisher;
import com.hei.school.entity.enums.BookFormat;
import com.hei.school.exception.BookNotFoundException;
import com.hei.school.exception.PublisherNotFoundException;
import com.hei.school.repository.BookEditionRepository;
import com.hei.school.repository.BookRepository;
import com.hei.school.repository.PriceHistoryRepository;
import com.hei.school.repository.PublisherRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookEditionServiceImplTest {

  @Mock private BookEditionRepository bookEditionRepository;
  @Mock private BookRepository bookRepository;
  @Mock private PublisherRepository publisherRepository;
  @Mock private PriceHistoryRepository priceHistoryRepository;
  @Mock private BookEditionMapper bookEditionMapper;
  @InjectMocks private BookEditionServiceImpl bookEditionService;

  @Test
  void getEditionsByBookIdShouldReturn() {
    var bookId = UUID.randomUUID();
    var edition = new BookEdition();
    var response = BookEditionResponse.builder().build();

    given(bookRepository.existsById(bookId)).willReturn(true);
    given(bookEditionRepository.findByBookId(bookId)).willReturn(List.of(edition));
    given(bookEditionMapper.toResponse(edition)).willReturn(response);

    var result = bookEditionService.getEditionsByBookId(bookId);

    assertEquals(1, result.size());
  }

  @Test
  void getEditionsByBookIdShouldThrowWhenBookNotFound() {
    given(bookRepository.existsById(any())).willReturn(false);

    assertThrows(
        BookNotFoundException.class,
        () -> bookEditionService.getEditionsByBookId(UUID.randomUUID()));
  }

  @Test
  void addEditionShouldPersistAndCreatePriceHistory() {
    var bookId = UUID.randomUUID();
    var publisherId = UUID.randomUUID();
    var book = new Book();
    book.setId(bookId);
    var publisher = new Publisher();
    publisher.setId(publisherId);
    var request =
        BookEditionCreateRequest.builder()
            .isbn("978-0141439518")
            .format(BookFormat.PAPERBACK)
            .sellingPrice(12.99)
            .publisherId(publisherId)
            .build();
    var edition = new BookEdition();
    edition.setSellingPrice(12.99);
    var saved = new BookEdition();
    saved.setSellingPrice(12.99);
    var response = BookEditionResponse.builder().build();

    given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
    given(bookEditionMapper.toEntity(request)).willReturn(edition);
    given(publisherRepository.findById(publisherId)).willReturn(Optional.of(publisher));
    given(bookEditionRepository.save(edition)).willReturn(saved);
    given(priceHistoryRepository.save(any(PriceHistory.class))).willReturn(null);
    given(bookEditionMapper.toResponse(saved)).willReturn(response);

    BookEditionResponse result = bookEditionService.addEdition(bookId, request);

    assertNotNull(result);
    then(bookEditionRepository).should().save(edition);
    then(priceHistoryRepository).should().save(any(PriceHistory.class));
  }

  @Test
  void addEditionShouldThrowWhenBookNotFound() {
    given(bookRepository.findById(any())).willReturn(Optional.empty());

    assertThrows(
        BookNotFoundException.class, () -> bookEditionService.addEdition(UUID.randomUUID(), any()));
  }

  @Test
  void addEditionShouldThrowWhenPublisherNotFound() {
    var bookId = UUID.randomUUID();
    var request =
        BookEditionCreateRequest.builder()
            .publisherId(UUID.randomUUID())
            .sellingPrice(10.0)
            .build();

    given(bookRepository.findById(bookId)).willReturn(Optional.of(new Book()));
    given(bookEditionMapper.toEntity(request)).willReturn(new BookEdition());
    given(publisherRepository.findById(any())).willReturn(Optional.empty());

    assertThrows(
        PublisherNotFoundException.class, () -> bookEditionService.addEdition(bookId, request));
  }

  @Test
  void updateEditionShouldModifyAndCreatePriceHistoryWhenPriceChanged() {
    var bookId = UUID.randomUUID();
    var editionId = UUID.randomUUID();
    var book = new Book();
    book.setId(bookId);
    var edition = new BookEdition();
    edition.setBook(book);
    edition.setSellingPrice(10.0);
    var request =
        BookEditionCreateRequest.builder()
            .isbn("978-0141439518")
            .format(BookFormat.PAPERBACK)
            .sellingPrice(15.0)
            .build();

    given(bookRepository.existsById(bookId)).willReturn(true);
    given(bookEditionRepository.findById(editionId)).willReturn(Optional.of(edition));
    given(bookEditionRepository.save(edition)).willReturn(edition);
    given(bookEditionMapper.toResponse(edition)).willReturn(BookEditionResponse.builder().build());

    bookEditionService.updateEdition(bookId, editionId, request);

    assertEquals(15.0, edition.getSellingPrice());
    then(priceHistoryRepository).should().save(any(PriceHistory.class));
  }

  @Test
  void deleteEditionShouldRemove() {
    var bookId = UUID.randomUUID();
    var editionId = UUID.randomUUID();
    var book = new Book();
    book.setId(bookId);
    var edition = new BookEdition();
    edition.setBook(book);

    given(bookRepository.existsById(bookId)).willReturn(true);
    given(bookEditionRepository.findById(editionId)).willReturn(Optional.of(edition));

    bookEditionService.deleteEdition(bookId, editionId);

    then(bookEditionRepository).should().delete(edition);
  }

  @Test
  void getEditionStockShouldReturn() {
    var bookId = UUID.randomUUID();
    var editionId = UUID.randomUUID();
    var book = new Book();
    book.setId(bookId);
    var edition = new BookEdition();
    edition.setBook(book);
    edition.setQuantityInStock(42);

    given(bookEditionRepository.findById(editionId)).willReturn(Optional.of(edition));

    Integer stock = bookEditionService.getEditionStock(bookId, editionId);

    assertEquals(42, stock);
  }

  @Test
  void getTotalBookStockShouldSum() {
    var bookId = UUID.randomUUID();
    var book = new Book();
    book.setId(bookId);
    var edition1 = new BookEdition();
    edition1.setQuantityInStock(10);
    var edition2 = new BookEdition();
    edition2.setQuantityInStock(20);

    given(bookRepository.existsById(bookId)).willReturn(true);
    given(bookEditionRepository.findByBookId(bookId)).willReturn(List.of(edition1, edition2));

    Integer total = bookEditionService.getTotalBookStock(bookId);

    assertEquals(30, total);
  }

  @Test
  void updateEditionStockShouldModify() {
    var bookId = UUID.randomUUID();
    var editionId = UUID.randomUUID();
    var book = new Book();
    book.setId(bookId);
    var edition = new BookEdition();
    edition.setBook(book);

    given(bookEditionRepository.findById(editionId)).willReturn(Optional.of(edition));
    given(bookEditionRepository.save(edition)).willReturn(edition);

    bookEditionService.updateEditionStock(bookId, editionId, 50);

    assertEquals(50, edition.getQuantityInStock());
  }
}
