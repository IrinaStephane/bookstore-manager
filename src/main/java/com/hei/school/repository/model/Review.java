package com.hei.school.repository.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Review {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String comment;
  private Integer rating;
  private LocalDateTime createdAt;
}