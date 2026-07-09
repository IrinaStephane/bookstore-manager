package com.hei.school.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import com.hei.school.endpoint.event.EventProducer;
import com.hei.school.endpoint.event.model.SaleConfirmedEvent;
import com.hei.school.endpoint.rest.model.SaleItemRequest;
import com.hei.school.endpoint.rest.model.SaleRequest;
import com.hei.school.entity.Book;
import com.hei.school.entity.BookEdition;
import com.hei.school.entity.Sale;
import com.hei.school.entity.enums.PaymentMethod;
import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.repository.BookEditionRepository;
import com.hei.school.repository.SaleRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaleServiceImplTest {

  @Mock private SaleRepository saleRepository;
  @Mock private BookEditionRepository bookEditionRepository;
  @Mock private EventProducer<SaleConfirmedEvent> eventProducer;
  @InjectMocks private SaleServiceImpl saleService;

  @Captor private ArgumentCaptor<Sale> saleCaptor;

  @Test
  void createSaleShouldPersistAndDecrementStock() {
    var editionId = UUID.randomUUID();
    var book = new Book();
    book.setTitle("1984");
    var edition = new BookEdition();
    edition.setId(editionId);
    edition.setQuantityInStock(10);
    edition.setSellingPrice(15.0);
    edition.setBook(book);
    edition.setIsbn("978-0141439518");

    var request =
        SaleRequest.builder()
            .userId(UUID.randomUUID())
            .email("test@test.com")
            .paymentMethod(PaymentMethod.CARD)
            .items(List.of(SaleItemRequest.builder().editionId(editionId).quantity(2).build()))
            .build();

    given(bookEditionRepository.findById(editionId)).willReturn(Optional.of(edition));
    given(bookEditionRepository.save(edition)).willReturn(edition);
    given(saleRepository.save(any(Sale.class))).willAnswer(inv -> inv.getArgument(0));

    saleService.createSale(request);

    assertEquals(8, edition.getQuantityInStock());
  }

  @Test
  void createSaleShouldThrowWhenEditionNotFound() {
    var request =
        SaleRequest.builder()
            .userId(UUID.randomUUID())
            .email("test@test.com")
            .paymentMethod(PaymentMethod.CARD)
            .items(
                List.of(SaleItemRequest.builder().editionId(UUID.randomUUID()).quantity(1).build()))
            .build();

    given(bookEditionRepository.findById(any())).willReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> saleService.createSale(request));
  }

  @Test
  void createSaleShouldThrowWhenInsufficientStock() {
    var editionId = UUID.randomUUID();
    var book = new Book();
    book.setTitle("1984");
    var edition = new BookEdition();
    edition.setId(editionId);
    edition.setQuantityInStock(1);
    edition.setSellingPrice(15.0);
    edition.setBook(book);

    var request =
        SaleRequest.builder()
            .userId(UUID.randomUUID())
            .email("test@test.com")
            .paymentMethod(PaymentMethod.CARD)
            .items(List.of(SaleItemRequest.builder().editionId(editionId).quantity(5).build()))
            .build();

    given(bookEditionRepository.findById(editionId)).willReturn(Optional.of(edition));

    assertThrows(IllegalArgumentException.class, () -> saleService.createSale(request));
  }

  @Test
  void createSaleShouldHandleEventProducerFailure() {
    var editionId = UUID.randomUUID();
    var book = new Book();
    book.setTitle("1984");
    var edition = new BookEdition();
    edition.setId(editionId);
    edition.setQuantityInStock(10);
    edition.setSellingPrice(15.0);
    edition.setBook(book);
    edition.setIsbn("978-0141439518");

    var request =
        SaleRequest.builder()
            .userId(UUID.randomUUID())
            .email("test@test.com")
            .paymentMethod(PaymentMethod.CARD)
            .items(List.of(SaleItemRequest.builder().editionId(editionId).quantity(1).build()))
            .build();

    given(bookEditionRepository.findById(editionId)).willReturn(Optional.of(edition));
    given(bookEditionRepository.save(edition)).willReturn(edition);
    given(saleRepository.save(any(Sale.class))).willAnswer(inv -> inv.getArgument(0));
    doThrow(new RuntimeException("Event failed")).when(eventProducer).accept(anyList());

    assertDoesNotThrow(() -> saleService.createSale(request));
  }
}
