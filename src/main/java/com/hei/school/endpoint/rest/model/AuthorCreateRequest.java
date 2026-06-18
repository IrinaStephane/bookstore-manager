package com.hei.school.endpoint.rest.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorCreateRequest {
  @NotBlank(message = "firstName is mandatory")
  @Size(max = 100)
  private String firstName;

  @NotBlank(message = "lastName is mandatory")
  @Size(max = 100)
  private String lastName;

  @Size(max = 2000)
  private String bio;
}
