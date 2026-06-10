package com.hei.school.endpoint.rest.model;

import com.hei.school.PojaGenerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PojaGenerated
public class AuthorResponse {
  private UUID id;
  private String firstName;
  private String lastName;
  private String bio;
}
