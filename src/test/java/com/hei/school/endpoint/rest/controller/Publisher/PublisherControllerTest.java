package com.hei.school.endpoint.rest.controller.Publisher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hei.school.endpoint.rest.model.PublisherCreateRequest;
import com.hei.school.endpoint.rest.model.PublisherResponse;
import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.service.PublisherService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PublisherController.class)
class PublisherControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private PublisherService publisherService;

  @Test
  void getAllPublishersShouldReturnList() throws Exception {
    var response = PublisherResponse.builder().id(UUID.randomUUID()).name("Penguin").build();

    given(publisherService.getAll()).willReturn(List.of(response));

    mockMvc.perform(get("/publishers"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Penguin"));
  }

  @Test
  void getPublisherByIdShouldReturn() throws Exception {
    var id = UUID.randomUUID();
    var response = PublisherResponse.builder().id(id).name("Penguin").build();

    given(publisherService.getById(id)).willReturn(response);

    mockMvc.perform(get("/publishers/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Penguin"));
  }

  @Test
  void getPublisherByIdShouldReturn404WhenNotFound() throws Exception {
    var id = UUID.randomUUID();
    given(publisherService.getById(id)).willThrow(new ResourceNotFoundException("not found"));

    mockMvc.perform(get("/publishers/{id}", id)).andExpect(status().isNotFound());
  }

  @Test
  void createPublisherShouldReturn201() throws Exception {
    var request = PublisherCreateRequest.builder().name("Penguin").country("UK").build();
    var response = PublisherResponse.builder().id(UUID.randomUUID()).name("Penguin").build();

    given(publisherService.create(any())).willReturn(response);

    mockMvc.perform(post("/publishers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Penguin"));
  }

  @Test
  void createPublisherShouldReturn400WhenInvalid() throws Exception {
    mockMvc.perform(post("/publishers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(PublisherCreateRequest.builder().build())))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deletePublisherShouldReturn204() throws Exception {
    mockMvc.perform(delete("/publishers/{id}", UUID.randomUUID()))
        .andExpect(status().isNoContent());
  }

  @Test
  void deletePublisherShouldReturn404WhenNotFound() throws Exception {
    var id = UUID.randomUUID();
    willThrow(new ResourceNotFoundException("not found")).given(publisherService).delete(id);

    mockMvc.perform(delete("/publishers/{id}", id)).andExpect(status().isNotFound());
  }
}
