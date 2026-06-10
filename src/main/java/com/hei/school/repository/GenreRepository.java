package com.hei.school.repository;

import com.hei.school.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface GenreRepository extends JpaRepository<Genre, UUID> {}
