package com.hei.school.endpoint.rest.controller.Stock;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hei.school.entity.Book;
import com.hei.school.entity.BookEdition;
import com.hei.school.repository.BookEditionRepository;
import com.hei.school.service.BookEditionService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StockController.class)
class StockControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private BookEditionRepository bookEditionRepository;
  @MockBean private BookEditionService bookEditionService;

  @Test
  void getEditionStockShouldReturn() throws Exception {
    var bookId = UUID.randomUUID();
    var editionId = UUID.randomUUID();
    var book = new Book();
    book.setId(bookId);
    var edition = new BookEdition();
    edition.setId(editionId);
    edition.setIsbn("978-0141439518");
    edition.setQuantityInStock(42);
    edition.setBook(book);

    given(bookEditionRepository.findById(editionId)).willReturn(Optional.of(edition));

    mockMvc
        .perform(get("/books/{bookId}/editions/{editionId}/stock", bookId, editionId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.quantityInStock").value(42));
  }

  @Test
  void getEditionStockShouldReturn404WhenNotFound() throws Exception {
    var editionId = UUID.randomUUID();
    given(bookEditionRepository.findById(editionId)).willReturn(Optional.empty());

    mockMvc
        .perform(get("/books/{bookId}/editions/{editionId}/stock", UUID.randomUUID(), editionId))
        .andExpect(status().isNotFound());
  }

  @Test
  void getTotalBookStockShouldReturn() throws Exception {
    var bookId = UUID.randomUUID();
    given(bookEditionService.getTotalBookStock(bookId)).willReturn(100);

    mockMvc
        .perform(get("/books/{bookId}/stock", bookId))
        .andExpect(status().isOk())
        .andExpect(content().string("100"));
  }

  @Test
  void updateEditionStockShouldReturn() throws Exception {
    var bookId = UUID.randomUUID();
    var editionId = UUID.randomUUID();
    var book = new Book();
    book.setId(bookId);
    var edition = new BookEdition();
    edition.setId(editionId);
    edition.setIsbn("978-0141439518");
    edition.setQuantityInStock(50);
    edition.setBook(book);

    given(bookEditionRepository.findById(editionId)).willReturn(Optional.of(edition));

    mockMvc
        .perform(
            patch("/books/{bookId}/editions/{editionId}/stock", bookId, editionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("50"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.quantityInStock").value(50));
  }

  @Test
  void updateEditionStockShouldReturn400WhenNegative() throws Exception {
    mockMvc
        .perform(
            patch(
                    "/books/{bookId}/editions/{editionId}/stock",
                    UUID.randomUUID(),
                    UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content("-5"))
        .andExpect(status().isBadRequest());
  }
}
