package com.hei.school.endpoint.rest.model;

import com.hei.school.PojaGenerated;
import com.hei.school.entity.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class BookCreateRequest {
  @NotBlank(message = "title is mandatory")
  @Size(max = 255)
  private String title;

  @Size(max = 2000)
  private String description;

  @NotNull(message = "language is mandatory")
  private Language language;

  @NotEmpty(message = "at least one authorId is required")
  private List<UUID> authorIds;

  private List<UUID> genreIds;
}
