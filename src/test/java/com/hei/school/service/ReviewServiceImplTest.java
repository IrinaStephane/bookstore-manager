package com.hei.school.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

  @Mock private ReviewRepository reviewRepository;
  @Mock private BookRepository bookRepository;
  @Mock private ReviewMapper reviewMapper;
  @InjectMocks private ReviewServiceImpl reviewService;

  @Test
  void getReviewsByBookIdShouldReturn() {
    var bookId = UUID.randomUUID();
    var review = new Review();
    var response = ReviewResponse.builder().build();

    given(bookRepository.existsById(bookId)).willReturn(true);
    given(reviewRepository.findByBookId(bookId)).willReturn(List.of(review));
    given(reviewMapper.toResponse(review)).willReturn(response);

    List<ReviewResponse> result = reviewService.getReviewsByBookId(bookId);

    assertEquals(1, result.size());
  }

  @Test
  void getReviewsByBookIdShouldThrowWhenBookNotFound() {
    given(bookRepository.existsById(any())).willReturn(false);

    assertThrows(
        BookNotFoundException.class, () -> reviewService.getReviewsByBookId(UUID.randomUUID()));
  }

  @Test
  void addReviewShouldPersistAndReturn() {
    var bookId = UUID.randomUUID();
    var book = new Book();
    book.setId(bookId);
    var request = ReviewCreateRequest.builder().rating(5).userId(UUID.randomUUID()).build();
    var review = new Review();
    var saved = new Review();
    var response = ReviewResponse.builder().build();

    given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
    given(reviewMapper.toEntity(request)).willReturn(review);
    given(reviewRepository.save(review)).willReturn(saved);
    given(reviewMapper.toResponse(saved)).willReturn(response);

    ReviewResponse result = reviewService.addReview(bookId, request);

    assertNotNull(result);
    assertEquals(book, review.getBook());
  }

  @Test
  void addReviewShouldThrowWhenBookNotFound() {
    given(bookRepository.findById(any())).willReturn(Optional.empty());

    assertThrows(
        BookNotFoundException.class, () -> reviewService.addReview(UUID.randomUUID(), any()));
  }

  @Test
  void deleteReviewShouldRemove() {
    var bookId = UUID.randomUUID();
    var reviewId = UUID.randomUUID();
    var book = new Book();
    book.setId(bookId);
    var review = new Review();
    review.setBook(book);

    given(bookRepository.existsById(bookId)).willReturn(true);
    given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

    reviewService.deleteReview(bookId, reviewId);

    then(reviewRepository).should().delete(review);
  }

  @Test
  void deleteReviewShouldThrowWhenBookNotFound() {
    given(bookRepository.existsById(any())).willReturn(false);

    assertThrows(
        BookNotFoundException.class,
        () -> reviewService.deleteReview(UUID.randomUUID(), UUID.randomUUID()));
  }

  @Test
  void deleteReviewShouldThrowWhenReviewNotFound() {
    var bookId = UUID.randomUUID();
    given(bookRepository.existsById(bookId)).willReturn(true);
    given(reviewRepository.findById(any())).willReturn(Optional.empty());

    assertThrows(
        ReviewNotFoundException.class, () -> reviewService.deleteReview(bookId, UUID.randomUUID()));
  }

  @Test
  void deleteReviewShouldThrowWhenReviewNotBelongToBook() {
    var bookId = UUID.randomUUID();
    var otherBookId = UUID.randomUUID();
    var review = new Review();
    var otherBook = new Book();
    otherBook.setId(otherBookId);
    review.setBook(otherBook);

    given(bookRepository.existsById(bookId)).willReturn(true);
    given(reviewRepository.findById(any())).willReturn(Optional.of(review));

    assertThrows(
        ReviewNotFoundException.class, () -> reviewService.deleteReview(bookId, UUID.randomUUID()));
  }
}
