package com.hei.school.repository;

import com.hei.school.entity.BookEdition;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookEditionRepository extends JpaRepository<BookEdition, UUID> {
  List<BookEdition> findByBookId(UUID bookId);
}
