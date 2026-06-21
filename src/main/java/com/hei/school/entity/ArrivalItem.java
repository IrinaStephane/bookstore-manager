package com.hei.school.entity;

import com.hei.school.entity.enums.BookCondition;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "arrival_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalItem {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private Double unitCost;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private BookCondition condition;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "arrival_id", nullable = false)
  private Arrival arrival;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "edition_id", nullable = false)
  private BookEdition edition;

  public Double getLineTotal() {
    return quantity * unitCost;
  }
}
