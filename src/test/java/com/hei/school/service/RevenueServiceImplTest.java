package com.hei.school.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.hei.school.endpoint.rest.model.GenreRevenueResponse;
import com.hei.school.entity.*;
import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.repository.GenreRepository;
import com.hei.school.repository.SaleItemRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RevenueServiceImplTest {

  @Mock private SaleItemRepository saleItemRepository;
  @Mock private GenreRepository genreRepository;
  @InjectMocks private RevenueServiceImpl revenueService;

  @Test
  void getRevenueByGenreShouldAggregate() {
    var fiction = new Genre();
    fiction.setId(UUID.randomUUID());
    fiction.setName("Fiction");

    var book = new Book();
    book.setGenres(List.of(fiction));

    var edition = new BookEdition();
    edition.setBook(book);

    var saleItem = new SaleItem();
    saleItem.setEdition(edition);
    saleItem.setQuantity(2);
    saleItem.setUnitPrice(10.0);

    given(saleItemRepository.findAll()).willReturn(List.of(saleItem));

    List<GenreRevenueResponse> result = revenueService.getRevenueByGenre();

    assertEquals(1, result.size());
    assertEquals("Fiction", result.get(0).getGenreName());
    assertEquals(20.0, result.get(0).getTotalRevenue());
    assertEquals(1, result.get(0).getSaleCount());
  }

  @Test
  void getRevenueByGenreShouldReturnEmptyWhenNoSales() {
    given(saleItemRepository.findAll()).willReturn(List.of());

    List<GenreRevenueResponse> result = revenueService.getRevenueByGenre();

    assertTrue(result.isEmpty());
  }

  @Test
  void getRevenueByGenreIdShouldReturn() {
    var genreId = UUID.randomUUID();
    var genre = new Genre();
    genre.setId(genreId);
    genre.setName("Science");

    var book = new Book();
    book.setGenres(List.of(genre));

    var edition = new BookEdition();
    edition.setBook(book);

    var saleItem = new SaleItem();
    saleItem.setEdition(edition);
    saleItem.setQuantity(3);
    saleItem.setUnitPrice(15.0);

    given(genreRepository.findById(genreId)).willReturn(Optional.of(genre));
    given(saleItemRepository.findByGenreId(genreId)).willReturn(List.of(saleItem));

    GenreRevenueResponse result = revenueService.getRevenueByGenre(genreId);

    assertEquals("Science", result.getGenreName());
    assertEquals(45.0, result.getTotalRevenue());
    assertEquals(1, result.getSaleCount());
  }

  @Test
  void getRevenueByGenreIdShouldThrowWhenGenreNotFound() {
    given(genreRepository.findById(any())).willReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class, () -> revenueService.getRevenueByGenre(UUID.randomUUID()));
  }

  @Test
  void getRevenueByGenreIdShouldReturnZeroWhenNoSales() {
    var genreId = UUID.randomUUID();
    var genre = new Genre();
    genre.setId(genreId);
    genre.setName("Empty");

    given(genreRepository.findById(genreId)).willReturn(Optional.of(genre));
    given(saleItemRepository.findByGenreId(genreId)).willReturn(List.of());

    GenreRevenueResponse result = revenueService.getRevenueByGenre(genreId);

    assertEquals(0.0, result.getTotalRevenue());
    assertEquals(0, result.getSaleCount());
  }
}
