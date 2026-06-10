package com.hei.school.endpoint.rest.model;

import com.hei.school.entity.enums.Language;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class BookPatchRequest {
  private String title;
  private String description;
  private Language language;
  private List<UUID> authorIds;
  private List<UUID> genreIds;
}
