package com.hei.school.entity;

import com.hei.school.entity.enums.BookCondition;
import com.hei.school.entity.enums.BookFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "book_edition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEdition {

  @Id @GeneratedValue @UuidGenerator private UUID id;

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
  @Builder.Default
  private Integer quantityInStock = 0;

  @Column(nullable = false)
  @Builder.Default
  private Integer totalReceived = 0;

  @Column(nullable = false)
  @Builder.Default
  private Integer totalSold = 0;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "publisher_id")
  private Publisher publisher;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  @OneToMany(mappedBy = "edition", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<PriceHistory> priceHistories = new java.util.ArrayList<>();

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
