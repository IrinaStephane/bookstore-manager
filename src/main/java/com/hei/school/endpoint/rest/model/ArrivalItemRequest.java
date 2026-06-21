package com.hei.school.endpoint.rest.model;

import com.hei.school.entity.enums.BookCondition;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArrivalItemRequest {
  @NotNull(message = "editionId is mandatory")
  private UUID editionId;

  @NotNull(message = "quantity is mandatory")
  @Positive(message = "quantity must be positive")
  private Integer quantity;

  @NotNull(message = "unitCost is mandatory")
  @Positive(message = "unitCost must be positive")
  private Double unitCost;

  private BookCondition condition;
}
