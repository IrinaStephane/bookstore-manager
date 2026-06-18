package com.hei.school.service;

import com.hei.school.endpoint.rest.model.ReviewCreateRequest;
import com.hei.school.endpoint.rest.model.ReviewResponse;
import java.util.List;
import java.util.UUID;

public interface ReviewService {

  List<ReviewResponse> getReviewsByBookId(UUID bookId);

  ReviewResponse addReview(UUID bookId, ReviewCreateRequest request);

  void deleteReview(UUID bookId, UUID reviewId);
}
