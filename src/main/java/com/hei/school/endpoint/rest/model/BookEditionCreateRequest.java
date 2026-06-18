package com.hei.school.endpoint.rest.model;

import com.hei.school.PojaGenerated;
import com.hei.school.entity.enums.BookCondition;
import com.hei.school.entity.enums.BookFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class BookEditionCreateRequest {

  @Size(max = 20)
  private String isbn;

  private BookFormat format;

  @NotNull(message = "sellingPrice is mandatory")
  @Positive(message = "sellingPrice must be positive")
  private Double sellingPrice;

  private LocalDate publishedDate;

  @Size(max = 500)
  private String coverImageUrl;

  private BookCondition condition;

  private Integer quantityInStock;

  private UUID publisherId;
}
