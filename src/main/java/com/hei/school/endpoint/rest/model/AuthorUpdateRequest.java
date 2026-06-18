package com.hei.school.endpoint.rest.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorUpdateRequest {
  @Size(max = 100)
  private String firstName;

  @Size(max = 100)
  private String lastName;

  @Size(max = 2000)
  private String bio;
}
