package com.hei.school.endpoint.rest.model;

import com.hei.school.entity.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class BookUpdateRequest {
  @NotBlank private String title;
  private String description;
  @NotNull private Language language;
  @NotEmpty private List<Long> authorIds;
  private List<Long> genreIds;
}
