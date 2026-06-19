package com.hei.school.entity;

import com.hei.school.entity.enums.PaymentMethod;
import com.hei.school.entity.enums.SaleStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "sale")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false, updatable = false)
  private LocalDateTime saleDate;

  @Column(nullable = false)
  private Double totalAmount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SaleStatus status;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SaleItem> items = new ArrayList<>();

  @PrePersist
  protected void onCreate() {
    this.saleDate = LocalDateTime.now();
  }
}
