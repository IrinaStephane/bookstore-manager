package com.hei.school.endpoint.rest.model;

import com.hei.school.entity.enums.BookCondition;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArrivalItemResponse {
  private UUID id;
  private Integer quantity;
  private Double unitCost;
  private Double lineTotal;
  private BookCondition condition;
  private UUID editionId;
  private String editionIsbn;
}
