package com.hei.school.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.hei.school.entity.enums.BookCondition;
import com.hei.school.entity.enums.BookFormat;
import com.hei.school.entity.enums.Language;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookEditionTest {

  @Mock private Book mockBook;

  @Mock private Publisher mockPublisher;

  private BookEdition bookEdition;

  @BeforeEach
  void setUp() {
    bookEdition =
        new BookEdition(
            null,
            "978-2-07-036024-5",
            BookFormat.PAPERBACK,
            14.99,
            LocalDate.of(2021, 3, 15),
            "https://cdn.library.mg/covers/test.jpg",
            BookCondition.NEW,
            50,
            50,
            0,
            mockPublisher,
            mockBook);
  }

  @Test
  void incrementStock_shouldIncreaseQuantityAndTotalReceived() {
    bookEdition.incrementStock(10);

    assertEquals(60, bookEdition.getQuantityInStock());
    assertEquals(60, bookEdition.getTotalReceived());
    assertEquals(0, bookEdition.getTotalSold());
  }

  @Test
  void incrementStock_withZero_shouldNotChangeValues() {
    bookEdition.incrementStock(0);

    assertEquals(50, bookEdition.getQuantityInStock());
    assertEquals(50, bookEdition.getTotalReceived());
  }

  @Test
  void decrementStock_shouldDecreaseQuantityAndIncreaseTotalSold() {
    bookEdition.decrementStock(5);

    assertEquals(45, bookEdition.getQuantityInStock());
    assertEquals(5, bookEdition.getTotalSold());
    assertEquals(50, bookEdition.getTotalReceived());
  }

  @Test
  void decrementStock_allStock_shouldResultInZeroQuantity() {
    bookEdition.decrementStock(50);

    assertEquals(0, bookEdition.getQuantityInStock());
    assertEquals(50, bookEdition.getTotalSold());
  }

  @Test
  void isLowStock_whenQuantityBelowThreshold_shouldReturnTrue() {
    bookEdition.decrementStock(45);

    assertTrue(bookEdition.isLowStock(10));
  }

  @Test
  void isLowStock_whenQuantityEqualsThreshold_shouldReturnTrue() {
    assertTrue(bookEdition.isLowStock(50));
  }

  @Test
  void isLowStock_whenQuantityAboveThreshold_shouldReturnFalse() {
    assertFalse(bookEdition.isLowStock(5));
  }

  @Test
  void bookEdition_shouldHoldCorrectBookReference() {
    when(mockBook.getTitle()).thenReturn("Les Misérables");
    when(mockBook.getLanguage()).thenReturn(Language.FR);

    assertEquals("Les Misérables", bookEdition.getBook().getTitle());
    assertEquals(Language.FR, bookEdition.getBook().getLanguage());

    verify(mockBook, times(1)).getTitle();
    verify(mockBook, times(1)).getLanguage();
  }

  @Test
  void bookEdition_shouldHoldCorrectPublisherReference() {
    when(mockPublisher.getName()).thenReturn("Gallimard");

    assertEquals("Gallimard", bookEdition.getPublisher().getName());

    verify(mockPublisher, times(1)).getName();
  }
}
