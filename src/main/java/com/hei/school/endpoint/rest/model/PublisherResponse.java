package com.hei.school.endpoint.rest.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublisherResponse {
  private UUID id;
  private String name;
  private String country;
}
