package com.hei.school.endpoint.rest.controller.Stock;

import com.hei.school.endpoint.rest.model.StockResponse;
import com.hei.school.entity.BookEdition;
import com.hei.school.exception.BadRequestException;
import com.hei.school.exception.BookEditionNotFoundException;
import com.hei.school.repository.BookEditionRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class StockController {

  private final BookEditionRepository bookEditionRepository;
  private final com.hei.school.service.BookEditionService bookEditionService;

  @GetMapping("/books/{bookId}/editions/{editionId}/stock")
  public StockResponse getEditionStock(@PathVariable UUID bookId, @PathVariable UUID editionId) {
    BookEdition edition = findEdition(bookId, editionId);
    return new StockResponse(editionId, edition.getIsbn(), edition.getQuantityInStock());
  }

  @GetMapping("/books/{bookId}/stock")
  public Integer getTotalBookStock(@PathVariable UUID bookId) {
    return bookEditionService.getTotalBookStock(bookId);
  }

  @PatchMapping("/books/{bookId}/editions/{editionId}/stock")
  public StockResponse updateEditionStock(
      @PathVariable UUID bookId, @PathVariable UUID editionId, @RequestBody Integer quantity) {
    if (quantity < 0) {
      throw new BadRequestException("Stock quantity cannot be negative");
    }
    bookEditionService.updateEditionStock(bookId, editionId, quantity);
    BookEdition edition = findEdition(bookId, editionId);
    return new StockResponse(editionId, edition.getIsbn(), quantity);
  }

  private BookEdition findEdition(UUID bookId, UUID editionId) {
    BookEdition edition =
        bookEditionRepository
            .findById(editionId)
            .orElseThrow(() -> new BookEditionNotFoundException(editionId));
    if (!edition.getBook().getId().equals(bookId)) {
      throw new BookEditionNotFoundException(
          "Book edition with id " + editionId + " does not belong to the specified book");
    }
    return edition;
  }
}
