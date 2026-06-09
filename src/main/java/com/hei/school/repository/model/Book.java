package com.hei.school.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Book {
  @Id private String id;
  private String title;
  private String author;
}