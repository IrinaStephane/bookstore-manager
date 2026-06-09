package com.hei.school.entity;

import com.hei.school.entity.enums.BookCondition;
import com.hei.school.entity.enums.BookFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "book_edition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookEdition {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, length = 20)
  private String isbn;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private BookFormat format;

  @Column(nullable = false)
  private Double sellingPrice;

  private LocalDate publishedDate;

  @Column(length = 500)
  private String coverImageUrl;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private BookCondition condition;

  @Column(nullable = false)
  private Integer quantityInStock = 0;

  @Column(nullable = false)
  private Integer totalReceived = 0;

  @Column(nullable = false)
  private Integer totalSold = 0;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "publisher_id")
  private Publisher publisher;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  public void incrementStock(int quantity) {
    this.quantityInStock += quantity;
    this.totalReceived += quantity;
  }

  public void decrementStock(int quantity) {
    this.quantityInStock -= quantity;
    this.totalSold += quantity;
  }

  public boolean isLowStock(int threshold) {
    return this.quantityInStock <= threshold;
  }
}
