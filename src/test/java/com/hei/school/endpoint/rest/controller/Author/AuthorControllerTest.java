package com.hei.school.endpoint.rest.controller.Author;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hei.school.endpoint.rest.model.AuthorCreateRequest;
import com.hei.school.endpoint.rest.model.AuthorResponse;
import com.hei.school.endpoint.rest.model.AuthorUpdateRequest;
import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.service.AuthorService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private AuthorService authorService;

  @Test
  void getAllAuthorsShouldReturnPage() throws Exception {
    var response = AuthorResponse.builder().id(UUID.randomUUID()).firstName("Jane").build();
    var page = new PageImpl<>(java.util.List.of(response), PageRequest.of(0, 20), 1);

    given(authorService.getAllAuthors(any(), any())).willReturn(page);

    mockMvc.perform(get("/authors"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].firstName").value("Jane"));
  }

  @Test
  void getAuthorByIdShouldReturn() throws Exception {
    var id = UUID.randomUUID();
    var response = AuthorResponse.builder().id(id).firstName("Jane").build();

    given(authorService.getById(id)).willReturn(response);

    mockMvc.perform(get("/authors/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()));
  }

  @Test
  void getAuthorByIdShouldReturn404WhenNotFound() throws Exception {
    var id = UUID.randomUUID();
    given(authorService.getById(id)).willThrow(new ResourceNotFoundException("not found"));

    mockMvc.perform(get("/authors/{id}", id)).andExpect(status().isNotFound());
  }

  @Test
  void createAuthorShouldReturn201() throws Exception {
    var request = AuthorCreateRequest.builder().firstName("Jane").lastName("Austen").build();
    var response = AuthorResponse.builder().id(UUID.randomUUID()).firstName("Jane").build();

    given(authorService.create(any())).willReturn(response);

    mockMvc.perform(post("/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.firstName").value("Jane"));
  }

  @Test
  void createAuthorShouldReturn400WhenInvalid() throws Exception {
    var request = AuthorCreateRequest.builder().lastName("Austen").build();

    mockMvc.perform(post("/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateAuthorShouldReturnOk() throws Exception {
    var id = UUID.randomUUID();
    var request = AuthorUpdateRequest.builder().firstName("Emily").build();
    var response = AuthorResponse.builder().id(id).firstName("Emily").build();

    given(authorService.update(any(), any())).willReturn(response);

    mockMvc.perform(put("/authors/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Emily"));
  }

  @Test
  void deleteAuthorShouldReturn204() throws Exception {
    var id = UUID.randomUUID();

    mockMvc.perform(delete("/authors/{id}", id)).andExpect(status().isNoContent());
  }

  @Test
  void deleteAuthorShouldReturn404WhenNotFound() throws Exception {
    var id = UUID.randomUUID();
    willThrow(new ResourceNotFoundException("not found")).given(authorService).delete(id);

    mockMvc.perform(delete("/authors/{id}", id)).andExpect(status().isNotFound());
  }
}
