package com.hei.school.endpoint.rest.model;

import com.hei.school.PojaGenerated;
import com.hei.school.entity.enums.Language;
import java.util.List;
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
public class BookResponse {
  private UUID id;
  private String title;
  private String description;
  private Language language;
  private List<AuthorSummary> authors;
  private List<GenreSummary> genres;
  private List<BookEditionResponse> editions;
  private List<ReviewResponse> reviews;
}
