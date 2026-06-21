package com.hei.school.endpoint.rest.model;

import com.hei.school.entity.enums.PaymentMethod;
import com.hei.school.entity.enums.SaleStatus;
import java.time.LocalDateTime;
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
public class SaleResponse {
  private UUID id;
  private LocalDateTime saleDate;
  private Double totalAmount;
  private PaymentMethod paymentMethod;
  private SaleStatus status;
  private UUID userId;
  private List<SaleItemResponse> items;
}
