package com.hei.school.endpoint.rest.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.hei.school.endpoint.rest.model.ReviewCreateRequest;
import com.hei.school.endpoint.rest.model.ReviewResponse;
import com.hei.school.entity.Review;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReviewMapperTest {

  private final ReviewMapper reviewMapper = new ReviewMapper();

  @Test
  void toResponseShouldMapAllFields() {
    var id = UUID.randomUUID();
    var userId = UUID.randomUUID();
    var now = LocalDateTime.now();
    var review = new Review();
    review.setId(id);
    review.setRating(4);
    review.setComment("Great book");
    review.setCreatedAt(now);
    review.setUserId(userId);

    ReviewResponse result = reviewMapper.toResponse(review);

    assertEquals(id, result.getId());
    assertEquals(4, result.getRating());
    assertEquals("Great book", result.getComment());
    assertEquals(now.atZone(ZoneId.systemDefault()).toInstant(), result.getCreatedAt());
    assertEquals(userId, result.getUserId());
  }

  @Test
  void toResponseShouldMapNullCreatedAt() {
    var review = new Review();
    review.setId(UUID.randomUUID());
    review.setRating(3);
    review.setUserId(UUID.randomUUID());

    ReviewResponse result = reviewMapper.toResponse(review);

    assertNull(result.getCreatedAt());
  }

  @Test
  void toResponseShouldMapNullComment() {
    var review = new Review();
    review.setId(UUID.randomUUID());
    review.setRating(5);
    review.setComment(null);
    review.setUserId(UUID.randomUUID());

    ReviewResponse result = reviewMapper.toResponse(review);

    assertNull(result.getComment());
  }

  @Test
  void toEntityShouldMapAllFields() {
    var userId = UUID.randomUUID();
    var request =
        ReviewCreateRequest.builder().rating(5).comment("Excellent").userId(userId).build();

    Review result = reviewMapper.toEntity(request);

    assertNull(result.getId());
    assertEquals(5, result.getRating());
    assertEquals("Excellent", result.getComment());
    assertEquals(userId, result.getUserId());
    assertNull(result.getBook());
  }

  @Test
  void toEntityShouldMapNullComment() {
    var userId = UUID.randomUUID();
    var request = ReviewCreateRequest.builder().rating(3).userId(userId).build();

    Review result = reviewMapper.toEntity(request);

    assertEquals(3, result.getRating());
    assertNull(result.getComment());
    assertEquals(userId, result.getUserId());
  }
}
