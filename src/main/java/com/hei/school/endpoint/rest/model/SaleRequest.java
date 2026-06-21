package com.hei.school.endpoint.rest.model;

import com.hei.school.entity.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class SaleRequest {
  @NotNull(message = "userId is mandatory")
  private UUID userId;

  @NotNull(message = "paymentMethod is mandatory")
  private PaymentMethod paymentMethod;

  @NotEmpty(message = "at least one item is required")
  @Valid
  private List<SaleItemRequest> items;
}
