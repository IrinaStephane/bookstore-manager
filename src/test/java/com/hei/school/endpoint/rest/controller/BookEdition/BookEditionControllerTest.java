package com.hei.school.endpoint.rest.controller.BookEdition;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hei.school.endpoint.rest.model.BookEditionCreateRequest;
import com.hei.school.endpoint.rest.model.BookEditionResponse;
import com.hei.school.entity.enums.BookFormat;
import com.hei.school.exception.BookNotFoundException;
import com.hei.school.service.BookEditionService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookEditionController.class)
class BookEditionControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private BookEditionService bookEditionService;

  @Test
  void getEditionsShouldReturnList() throws Exception {
    var bookId = UUID.randomUUID();
    var response =
        BookEditionResponse.builder().id(UUID.randomUUID()).isbn("978-0141439518").build();

    given(bookEditionService.getEditionsByBookId(bookId)).willReturn(List.of(response));

    mockMvc
        .perform(get("/books/{bookId}/editions", bookId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].isbn").value("978-0141439518"));
  }

  @Test
  void getEditionsShouldReturn404WhenBookNotFound() throws Exception {
    var bookId = UUID.randomUUID();
    given(bookEditionService.getEditionsByBookId(bookId))
        .willThrow(new BookNotFoundException(bookId));

    mockMvc.perform(get("/books/{bookId}/editions", bookId)).andExpect(status().isNotFound());
  }

  @Test
  void addEditionShouldReturn201() throws Exception {
    var bookId = UUID.randomUUID();
    var request =
        BookEditionCreateRequest.builder()
            .isbn("978-0141439518")
            .format(BookFormat.PAPERBACK)
            .sellingPrice(12.99)
            .build();
    var response =
        BookEditionResponse.builder().id(UUID.randomUUID()).isbn("978-0141439518").build();

    given(bookEditionService.addEdition(any(), any())).willReturn(response);

    mockMvc
        .perform(
            post("/books/{bookId}/editions", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.isbn").value("978-0141439518"));
  }

  @Test
  void addEditionShouldReturn400WhenInvalid() throws Exception {
    mockMvc
        .perform(
            post("/books/{bookId}/editions", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(BookEditionCreateRequest.builder().build())))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateEditionShouldReturnOk() throws Exception {
    var bookId = UUID.randomUUID();
    var editionId = UUID.randomUUID();
    var request =
        BookEditionCreateRequest.builder()
            .isbn("978-0141439518")
            .format(BookFormat.HARDCOVER)
            .sellingPrice(15.99)
            .build();
    var response = BookEditionResponse.builder().id(editionId).isbn("978-0141439518").build();

    given(bookEditionService.updateEdition(any(), any(), any())).willReturn(response);

    mockMvc
        .perform(
            put("/books/{bookId}/editions/{id}", bookId, editionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isbn").value("978-0141439518"));
  }

  @Test
  void deleteEditionShouldReturn204() throws Exception {
    mockMvc
        .perform(delete("/books/{bookId}/editions/{id}", UUID.randomUUID(), UUID.randomUUID()))
        .andExpect(status().isNoContent());
  }
}
