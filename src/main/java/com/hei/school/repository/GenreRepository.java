package com.hei.school.repository;

import com.hei.school.entity.Genre;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, UUID> {
  boolean existsByNameIgnoreCase(String name);
}
