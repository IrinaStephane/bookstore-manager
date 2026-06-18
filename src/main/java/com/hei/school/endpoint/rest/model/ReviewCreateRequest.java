package com.hei.school.endpoint.rest.model;

import com.hei.school.PojaGenerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class ReviewCreateRequest {

  @NotNull(message = "rating is mandatory")
  @Min(1)
  @Max(5)
  private Integer rating;

  @Size(max = 2000)
  private String comment;

  @NotNull(message = "userId is mandatory")
  private UUID userId;
}
