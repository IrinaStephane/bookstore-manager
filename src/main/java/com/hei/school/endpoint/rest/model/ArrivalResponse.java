package com.hei.school.endpoint.rest.model;

import java.time.LocalDate;
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
public class ArrivalResponse {
  private UUID id;
  private LocalDate arrivalDate;
  private String notes;
  private Double totalCost;
  private List<ArrivalItemResponse> items;
}
