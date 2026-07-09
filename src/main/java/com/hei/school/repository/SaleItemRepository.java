package com.hei.school.repository;

import com.hei.school.entity.SaleItem;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SaleItemRepository extends JpaRepository<SaleItem, UUID> {

  @Query(
      "SELECT si FROM SaleItem si JOIN si.edition e JOIN e.book b JOIN b.genres g WHERE g.id ="
          + " :genreId")
  List<SaleItem> findByGenreId(@Param("genreId") UUID genreId);
}
