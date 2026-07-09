package com.hei.school.endpoint.rest.controller.Genre;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hei.school.endpoint.rest.model.GenreCreateRequest;
import com.hei.school.endpoint.rest.model.GenreResponse;
import com.hei.school.exception.DuplicateResourceException;
import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.service.GenreService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GenreController.class)
class GenreControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private GenreService genreService;

  @Test
  void getAllGenresShouldReturnList() throws Exception {
    var response = GenreResponse.builder().id(UUID.randomUUID()).name("Fiction").build();

    given(genreService.getAll()).willReturn(List.of(response));

    mockMvc
        .perform(get("/genres"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Fiction"));
  }

  @Test
  void getGenreByIdShouldReturn() throws Exception {
    var id = UUID.randomUUID();
    var response = GenreResponse.builder().id(id).name("Fiction").build();

    given(genreService.getById(id)).willReturn(response);

    mockMvc
        .perform(get("/genres/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Fiction"));
  }

  @Test
  void getGenreByIdShouldReturn404WhenNotFound() throws Exception {
    var id = UUID.randomUUID();
    given(genreService.getById(id)).willThrow(new ResourceNotFoundException("not found"));

    mockMvc.perform(get("/genres/{id}", id)).andExpect(status().isNotFound());
  }

  @Test
  void createGenreShouldReturn201() throws Exception {
    var request = GenreCreateRequest.builder().name("Fiction").build();
    var response = GenreResponse.builder().id(UUID.randomUUID()).name("Fiction").build();

    given(genreService.create(any())).willReturn(response);

    mockMvc
        .perform(
            post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Fiction"));
  }

  @Test
  void createGenreShouldReturn400WhenInvalid() throws Exception {
    mockMvc
        .perform(
            post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(GenreCreateRequest.builder().build())))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createGenreShouldReturn409WhenDuplicate() throws Exception {
    var request = GenreCreateRequest.builder().name("Fiction").build();

    given(genreService.create(any())).willThrow(new DuplicateResourceException("duplicate"));

    mockMvc
        .perform(
            post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isConflict());
  }

  @Test
  void deleteGenreShouldReturn204() throws Exception {
    mockMvc.perform(delete("/genres/{id}", UUID.randomUUID())).andExpect(status().isNoContent());
  }

  @Test
  void deleteGenreShouldReturn404WhenNotFound() throws Exception {
    var id = UUID.randomUUID();
    willThrow(new ResourceNotFoundException("not found")).given(genreService).delete(id);

    mockMvc.perform(delete("/genres/{id}", id)).andExpect(status().isNotFound());
  }
}
