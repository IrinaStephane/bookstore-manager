package com.hei.school.repository;

import com.hei.school.entity.SaleItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleItemRepository extends JpaRepository<SaleItem, UUID> {}
