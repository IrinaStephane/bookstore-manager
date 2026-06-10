package com.hei.school.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "genre")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 100)
  private String name;
}
