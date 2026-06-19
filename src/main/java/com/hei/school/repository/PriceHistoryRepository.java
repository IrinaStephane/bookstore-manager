package com.hei.school.repository;

import com.hei.school.entity.PriceHistory;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, UUID> {
  List<PriceHistory> findByEditionIdOrderByEffectiveDateAsc(UUID editionId);
}
