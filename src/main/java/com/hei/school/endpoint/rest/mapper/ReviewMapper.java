package com.hei.school.endpoint.rest.mapper;

import com.hei.school.PojaGenerated;
import com.hei.school.endpoint.rest.model.ReviewCreateRequest;
import com.hei.school.endpoint.rest.model.ReviewResponse;
import com.hei.school.entity.Review;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

@Component
@PojaGenerated
public class ReviewMapper {

  public ReviewResponse toResponse(Review domain) {
    return ReviewResponse.builder()
        .id(domain.getId())
        .rating(domain.getRating())
        .comment(domain.getComment())
        .createdAt(
            domain.getCreatedAt() == null
                ? null
                : domain.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant())
        .userId(domain.getUserId())
        .build();
  }

  public Review toEntity(ReviewCreateRequest request) {
    Review review = new Review();
    review.setRating(request.getRating());
    review.setComment(request.getComment());
    review.setUserId(request.getUserId());
    return review;
  }
}
