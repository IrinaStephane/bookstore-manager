package com.hei.school.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "publisher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 150)
  private String name;

  @Column(length = 100)
  private String country;
}
