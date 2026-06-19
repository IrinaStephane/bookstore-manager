package com.hei.school.service;

import com.hei.school.endpoint.rest.mapper.ReviewMapper;
import com.hei.school.endpoint.rest.model.ReviewCreateRequest;
import com.hei.school.endpoint.rest.model.ReviewResponse;
import com.hei.school.entity.Book;
import com.hei.school.entity.Review;
import com.hei.school.exception.BookNotFoundException;
import com.hei.school.exception.ReviewNotFoundException;
import com.hei.school.repository.BookRepository;
import com.hei.school.repository.ReviewRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepository;
  private final BookRepository bookRepository;
  private final ReviewMapper reviewMapper;

  @Override
  public List<ReviewResponse> getReviewsByBookId(UUID bookId) {
    ensureBookExists(bookId);
    return reviewRepository.findByBookId(bookId).stream().map(reviewMapper::toResponse).toList();
  }

  @Override
  @Transactional
  public ReviewResponse addReview(UUID bookId, ReviewCreateRequest request) {
    Book book = findBookOrThrow(bookId);

    Review review = reviewMapper.toEntity(request);
    review.setBook(book);

    return reviewMapper.toResponse(reviewRepository.save(review));
  }

  @Override
  @Transactional
  public void deleteReview(UUID bookId, UUID reviewId) {
    ensureBookExists(bookId);
    Review review = findReviewOrThrow(bookId, reviewId);
    reviewRepository.delete(review);
  }

  private Book findBookOrThrow(UUID bookId) {
    return bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
  }

  private void ensureBookExists(UUID bookId) {
    if (!bookRepository.existsById(bookId)) {
      throw new BookNotFoundException(bookId);
    }
  }

  private Review findReviewOrThrow(UUID bookId, UUID reviewId) {
    Review review =
        reviewRepository
            .findById(reviewId)
            .orElseThrow(() -> new ReviewNotFoundException(reviewId));
    if (!review.getBook().getId().equals(bookId)) {
      throw new ReviewNotFoundException(reviewId);
    }
    return review;
  }
}
