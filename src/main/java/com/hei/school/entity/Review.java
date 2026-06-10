package com.hei.school.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Integer rating;

  @Column(columnDefinition = "TEXT")
  private String comment;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private Long userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }
}
