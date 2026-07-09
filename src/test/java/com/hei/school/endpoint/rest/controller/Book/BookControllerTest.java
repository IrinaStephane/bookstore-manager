package com.hei.school.endpoint.rest.controller.Book;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hei.school.endpoint.rest.model.BookCreateRequest;
import com.hei.school.endpoint.rest.model.BookPatchRequest;
import com.hei.school.endpoint.rest.model.BookResponse;
import com.hei.school.endpoint.rest.model.BookUpdateRequest;
import com.hei.school.entity.enums.Language;
import com.hei.school.exception.BookNotFoundException;
import com.hei.school.service.BookService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
class BookControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private BookService bookService;

  @Test
  void getAllBooksShouldReturnPage() throws Exception {
    var response = BookResponse.builder().id(UUID.randomUUID()).title("1984").build();
    var page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);

    given(bookService.getAllBooks(any(), any(), any(), any(), any())).willReturn(page);

    mockMvc
        .perform(get("/books"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].title").value("1984"));
  }

  @Test
  void getBookByIdShouldReturn() throws Exception {
    var id = UUID.randomUUID();
    var response = BookResponse.builder().id(id).title("1984").build();

    given(bookService.getBookById(id)).willReturn(response);

    mockMvc
        .perform(get("/books/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("1984"));
  }

  @Test
  void getBookByIdShouldReturn404WhenNotFound() throws Exception {
    var id = UUID.randomUUID();
    given(bookService.getBookById(id)).willThrow(new BookNotFoundException(id));

    mockMvc.perform(get("/books/{id}", id)).andExpect(status().isNotFound());
  }

  @Test
  void createBookShouldReturn201() throws Exception {
    var request =
        BookCreateRequest.builder()
            .title("1984")
            .language(Language.EN)
            .authorIds(List.of(UUID.randomUUID()))
            .build();
    var response = BookResponse.builder().id(UUID.randomUUID()).title("1984").build();

    given(bookService.createBook(any())).willReturn(response);

    mockMvc
        .perform(
            post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("1984"));
  }

  @Test
  void createBookShouldReturn400WhenInvalid() throws Exception {
    mockMvc
        .perform(
            post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(BookCreateRequest.builder().build())))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateBookShouldReturnOk() throws Exception {
    var id = UUID.randomUUID();
    var request = new BookUpdateRequest();
    request.setTitle("1984");
    request.setLanguage(Language.EN);
    request.setAuthorIds(List.of());
    var response = BookResponse.builder().id(id).title("1984").build();

    given(bookService.updateBook(any(), any())).willReturn(response);

    mockMvc
        .perform(
            put("/books/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("1984"));
  }

  @Test
  void patchBookShouldReturnOk() throws Exception {
    var id = UUID.randomUUID();
    var request = new BookPatchRequest();
    request.setTitle("Updated");
    var response = BookResponse.builder().id(id).title("Updated").build();

    given(bookService.patchBook(any(), any())).willReturn(response);

    mockMvc
        .perform(
            patch("/books/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Updated"));
  }

  @Test
  void deleteBookShouldReturnOk() throws Exception {
    mockMvc.perform(delete("/books/{id}", UUID.randomUUID())).andExpect(status().isOk());
  }
}
