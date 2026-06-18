package com.hei.school.endpoint.rest.controller.Review;

import com.hei.school.PojaGenerated;
import com.hei.school.endpoint.rest.model.ReviewCreateRequest;
import com.hei.school.endpoint.rest.model.ReviewResponse;
import com.hei.school.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books/{bookId}/reviews")
@AllArgsConstructor
@PojaGenerated
public class ReviewController {

  private final ReviewService reviewService;

  @GetMapping
  public List<ReviewResponse> getReviews(@PathVariable UUID bookId) {
    return reviewService.getReviewsByBookId(bookId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ReviewResponse addReview(
      @PathVariable UUID bookId, @Valid @RequestBody ReviewCreateRequest request) {
    return reviewService.addReview(bookId, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteReview(@PathVariable UUID bookId, @PathVariable UUID id) {
    reviewService.deleteReview(bookId, id);
  }
}
