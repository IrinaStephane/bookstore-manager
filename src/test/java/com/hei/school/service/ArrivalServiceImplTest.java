package com.hei.school.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.hei.school.endpoint.rest.model.ArrivalItemRequest;
import com.hei.school.endpoint.rest.model.ArrivalRequest;
import com.hei.school.entity.Arrival;
import com.hei.school.entity.Book;
import com.hei.school.entity.BookEdition;
import com.hei.school.entity.enums.BookCondition;
import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.repository.ArrivalRepository;
import com.hei.school.repository.BookEditionRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArrivalServiceImplTest {

  @Mock private ArrivalRepository arrivalRepository;
  @Mock private BookEditionRepository bookEditionRepository;
  @InjectMocks private ArrivalServiceImpl arrivalService;

  @Test
  void createArrivalShouldIncrementStock() {
    var editionId = UUID.randomUUID();
    var book = new Book();
    book.setTitle("1984");
    var edition = new BookEdition();
    edition.setId(editionId);
    edition.setQuantityInStock(10);
    edition.setTotalReceived(10);
    edition.setBook(book);
    edition.setIsbn("978-0141439518");

    var request = ArrivalRequest.builder()
        .arrivalDate(LocalDate.now())
        .items(List.of(ArrivalItemRequest.builder()
            .editionId(editionId)
            .quantity(5)
            .unitCost(10.0)
            .condition(BookCondition.NEW)
            .build()))
        .build();

    given(bookEditionRepository.findById(editionId)).willReturn(Optional.of(edition));
    given(bookEditionRepository.save(edition)).willReturn(edition);
    given(arrivalRepository.save(any(Arrival.class)))
        .willAnswer(inv -> inv.getArgument(0));

    arrivalService.createArrival(request);

    assertEquals(15, edition.getQuantityInStock());
    assertEquals(15, edition.getTotalReceived());
  }

  @Test
  void createArrivalShouldThrowWhenEditionNotFound() {
    var request = ArrivalRequest.builder()
        .arrivalDate(LocalDate.now())
        .items(List.of(ArrivalItemRequest.builder()
            .editionId(UUID.randomUUID())
            .quantity(5)
            .unitCost(10.0)
            .build()))
        .build();

    given(bookEditionRepository.findById(any())).willReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> arrivalService.createArrival(request));
  }
}
