package com.hei.school.service;

import com.hei.school.endpoint.rest.mapper.BookEditionMapper;
import com.hei.school.endpoint.rest.model.BookEditionCreateRequest;
import com.hei.school.endpoint.rest.model.BookEditionResponse;
import com.hei.school.entity.Book;
import com.hei.school.entity.BookEdition;
import com.hei.school.entity.PriceHistory;
import com.hei.school.entity.Publisher;
import com.hei.school.exception.BookEditionNotFoundException;
import com.hei.school.exception.BookNotFoundException;
import com.hei.school.exception.PublisherNotFoundException;
import com.hei.school.repository.BookEditionRepository;
import com.hei.school.repository.BookRepository;
import com.hei.school.repository.PriceHistoryRepository;
import com.hei.school.repository.PublisherRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookEditionServiceImpl implements BookEditionService {

  private final BookEditionRepository bookEditionRepository;
  private final BookRepository bookRepository;
  private final PublisherRepository publisherRepository;
  private final PriceHistoryRepository priceHistoryRepository;
  private final BookEditionMapper bookEditionMapper;

  @Override
  public List<BookEditionResponse> getEditionsByBookId(UUID bookId) {
    ensureBookExists(bookId);
    return bookEditionRepository.findByBookId(bookId).stream()
        .map(bookEditionMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional
  public BookEditionResponse addEdition(UUID bookId, BookEditionCreateRequest request) {
    Book book = findBookOrThrow(bookId);

    BookEdition edition = bookEditionMapper.toEntity(request);
    edition.setBook(book);
    edition.setPublisher(resolvePublisher(request.getPublisherId()));
    edition = bookEditionRepository.save(edition);

    PriceHistory history =
        PriceHistory.builder()
            .edition(edition)
            .price(edition.getSellingPrice())
            .effectiveDate(LocalDate.now())
            .build();
    priceHistoryRepository.save(history);

    return bookEditionMapper.toResponse(edition);
  }

  @Override
  @Transactional
  public BookEditionResponse updateEdition(
      UUID bookId, UUID editionId, BookEditionCreateRequest request) {
    ensureBookExists(bookId);
    BookEdition edition = findEditionOrThrow(bookId, editionId);

    edition.setIsbn(request.getIsbn());
    edition.setFormat(request.getFormat());
    edition.setSellingPrice(request.getSellingPrice());
    edition.setPublishedDate(request.getPublishedDate());
    edition.setCoverImageUrl(request.getCoverImageUrl());
    edition.setCondition(request.getCondition());
    if (request.getQuantityInStock() != null) {
      edition.setQuantityInStock(request.getQuantityInStock());
    }
    edition.setPublisher(resolvePublisher(request.getPublisherId()));

    Double oldPrice = edition.getSellingPrice();
    Double newPrice = request.getSellingPrice();
    if (!newPrice.equals(oldPrice)) {
      edition.setSellingPrice(newPrice);
      PriceHistory history =
          PriceHistory.builder()
              .edition(edition)
              .price(newPrice)
              .effectiveDate(LocalDate.now())
              .build();
      priceHistoryRepository.save(history);
    }

    return bookEditionMapper.toResponse(bookEditionRepository.save(edition));
  }

  @Override
  @Transactional
  public void deleteEdition(UUID bookId, UUID editionId) {
    ensureBookExists(bookId);
    BookEdition edition = findEditionOrThrow(bookId, editionId);
    bookEditionRepository.delete(edition);
  }

  @Override
  @Transactional(readOnly = true)
  public Integer getEditionStock(UUID bookId, UUID editionId) {
    BookEdition edition = findEditionOrThrow(bookId, editionId);
    return edition.getQuantityInStock();
  }

  @Override
  @Transactional(readOnly = true)
  public Integer getTotalBookStock(UUID bookId) {
    ensureBookExists(bookId);
    return bookEditionRepository.findByBookId(bookId).stream()
        .mapToInt(BookEdition::getQuantityInStock)
        .sum();
  }

  @Override
  @Transactional
  public void updateEditionStock(UUID bookId, UUID editionId, Integer quantity) {
    BookEdition edition = findEditionOrThrow(bookId, editionId);
    edition.setQuantityInStock(quantity);
    bookEditionRepository.save(edition);
  }

  private Book findBookOrThrow(UUID bookId) {
    return bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
  }

  private void ensureBookExists(UUID bookId) {
    if (!bookRepository.existsById(bookId)) {
      throw new BookNotFoundException(bookId);
    }
  }

  private BookEdition findEditionOrThrow(UUID bookId, UUID editionId) {
    BookEdition edition =
        bookEditionRepository
            .findById(editionId)
            .orElseThrow(() -> new BookEditionNotFoundException(editionId));
    if (!edition.getBook().getId().equals(bookId)) {
      throw new BookEditionNotFoundException(editionId);
    }
    return edition;
  }

  private Publisher resolvePublisher(UUID publisherId) {
    if (publisherId == null) {
      return null;
    }
    return publisherRepository
        .findById(publisherId)
        .orElseThrow(() -> new PublisherNotFoundException(publisherId));
  }
}
