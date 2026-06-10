package com.hei.school.endpoint.rest.model;

import com.hei.school.PojaGenerated;
import com.hei.school.entity.enums.BookCondition;
import com.hei.school.entity.enums.BookFormat;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PojaGenerated
public class BookEditionResponse {
  private UUID id;
  private String isbn;
  private BookFormat format;
  private Double sellingPrice;
  private LocalDate publishedDate;
  private String coverImageUrl;
  private BookCondition condition;
  private Integer quantityInStock;
  private Integer totalReceived;
  private Integer totalSold;
  private PublisherSummary publisher;
}
