package com.hei.school.endpoint.rest.controller.Review;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hei.school.endpoint.rest.model.ReviewCreateRequest;
import com.hei.school.endpoint.rest.model.ReviewResponse;
import com.hei.school.exception.BookNotFoundException;
import com.hei.school.exception.ReviewNotFoundException;
import com.hei.school.service.ReviewService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private ReviewService reviewService;

  @Test
  void getReviewsShouldReturnList() throws Exception {
    var bookId = UUID.randomUUID();
    var response = ReviewResponse.builder().id(UUID.randomUUID()).rating(5).build();

    given(reviewService.getReviewsByBookId(bookId)).willReturn(List.of(response));

    mockMvc.perform(get("/books/{bookId}/reviews", bookId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].rating").value(5));
  }

  @Test
  void getReviewsShouldReturn404WhenBookNotFound() throws Exception {
    var bookId = UUID.randomUUID();
    given(reviewService.getReviewsByBookId(bookId))
        .willThrow(new BookNotFoundException(bookId));

    mockMvc.perform(get("/books/{bookId}/reviews", bookId))
        .andExpect(status().isNotFound());
  }

  @Test
  void addReviewShouldReturn201() throws Exception {
    var bookId = UUID.randomUUID();
    var request = ReviewCreateRequest.builder()
        .rating(5).userId(UUID.randomUUID()).build();
    var response = ReviewResponse.builder().id(UUID.randomUUID()).rating(5).build();

    given(reviewService.addReview(any(), any())).willReturn(response);

    mockMvc.perform(post("/books/{bookId}/reviews", bookId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.rating").value(5));
  }

  @Test
  void addReviewShouldReturn400WhenInvalid() throws Exception {
    mockMvc.perform(post("/books/{bookId}/reviews", UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(ReviewCreateRequest.builder().build())))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteReviewShouldReturn204() throws Exception {
    mockMvc.perform(delete("/books/{bookId}/reviews/{id}", UUID.randomUUID(), UUID.randomUUID()))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteReviewShouldReturn404WhenNotFound() throws Exception {
    var bookId = UUID.randomUUID();
    var reviewId = UUID.randomUUID();
    willThrow(new ReviewNotFoundException(reviewId))
        .given(reviewService).deleteReview(bookId, reviewId);

    mockMvc.perform(delete("/books/{bookId}/reviews/{id}", bookId, reviewId))
        .andExpect(status().isNotFound());
  }
}
