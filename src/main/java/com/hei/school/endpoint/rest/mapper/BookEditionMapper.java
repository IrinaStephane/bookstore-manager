package com.hei.school.endpoint.rest.mapper;

import com.hei.school.PojaGenerated;
import com.hei.school.endpoint.rest.model.BookEditionCreateRequest;
import com.hei.school.endpoint.rest.model.BookEditionResponse;
import com.hei.school.endpoint.rest.model.PublisherSummary;
import com.hei.school.entity.BookEdition;
import com.hei.school.entity.Publisher;
import org.springframework.stereotype.Component;

@Component
@PojaGenerated
public class BookEditionMapper {

  public BookEditionResponse toResponse(BookEdition domain) {
    return BookEditionResponse.builder()
        .id(domain.getId())
        .isbn(domain.getIsbn())
        .format(domain.getFormat())
        .sellingPrice(domain.getSellingPrice())
        .publishedDate(domain.getPublishedDate())
        .coverImageUrl(domain.getCoverImageUrl())
        .condition(domain.getCondition())
        .quantityInStock(domain.getQuantityInStock())
        .totalReceived(domain.getTotalReceived())
        .totalSold(domain.getTotalSold())
        .publisher(domain.getPublisher() == null ? null : toPublisherSummary(domain.getPublisher()))
        .build();
  }

  public BookEdition toEntity(BookEditionCreateRequest request) {
    BookEdition edition = new BookEdition();
    edition.setIsbn(request.getIsbn());
    edition.setFormat(request.getFormat());
    edition.setSellingPrice(request.getSellingPrice());
    edition.setPublishedDate(request.getPublishedDate());
    edition.setCoverImageUrl(request.getCoverImageUrl());
    edition.setCondition(request.getCondition());
    edition.setQuantityInStock(
        request.getQuantityInStock() == null ? 0 : request.getQuantityInStock());
    return edition;
  }

  private PublisherSummary toPublisherSummary(Publisher publisher) {
    return PublisherSummary.builder()
        .id(publisher.getId())
        .name(publisher.getName())
        .country(publisher.getCountry())
        .build();
  }
}
