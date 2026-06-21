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
public class SaleItemResponse {
  private UUID id;
  private Integer quantity;
  private Double unitPrice;
  private Double lineTotal;
  private UUID editionId;
  private String editionIsbn;
}
