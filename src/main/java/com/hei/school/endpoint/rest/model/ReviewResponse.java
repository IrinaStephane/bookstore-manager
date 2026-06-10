package com.hei.school.endpoint.rest.model;

import com.hei.school.PojaGenerated;
import java.time.Instant;
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
public class ReviewResponse {
  private UUID id;
  private Integer rating;
  private String comment;
  private Instant createdAt;
  private UUID userId;
}
