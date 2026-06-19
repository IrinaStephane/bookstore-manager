package com.hei.school.endpoint.rest.controller.Stock;

import com.hei.school.endpoint.rest.model.StockResponse;
import com.hei.school.service.BookEditionService;
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

  private final BookEditionService bookEditionService;

  @GetMapping("/books/{bookId}/editions/{editionId}/stock")
  public StockResponse getEditionStock(
      @PathVariable UUID bookId, @PathVariable UUID editionId) {
    Integer quantity = bookEditionService.getEditionStock(bookId, editionId);
    return new StockResponse(editionId, null, quantity);
  }

  @GetMapping("/books/{bookId}/stock")
  public Integer getTotalBookStock(@PathVariable UUID bookId) {
    return bookEditionService.getTotalBookStock(bookId);
  }

  @PatchMapping("/books/{bookId}/editions/{editionId}/stock")
  public StockResponse updateEditionStock(
      @PathVariable UUID bookId,
      @PathVariable UUID editionId,
      @RequestBody Integer quantity) {
    bookEditionService.updateEditionStock(bookId, editionId, quantity);
    return new StockResponse(editionId, null, quantity);
  }
}
