package com.hei.school.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "arrival")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Arrival {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false)
  private LocalDate arrivalDate;

  @Column(columnDefinition = "TEXT")
  private String notes;

  @OneToMany(mappedBy = "arrival", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ArrivalItem> items = new ArrayList<>();
}
