package com.hei.school.endpoint.rest.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArrivalRequest {
  @NotNull(message = "arrivalDate is mandatory")
  private LocalDate arrivalDate;

  private String notes;

  @NotEmpty(message = "at least one item is required")
  @Valid
  private List<ArrivalItemRequest> items;
}
