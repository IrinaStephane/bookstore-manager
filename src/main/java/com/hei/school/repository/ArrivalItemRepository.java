package com.hei.school.repository;

import com.hei.school.entity.ArrivalItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrivalItemRepository extends JpaRepository<ArrivalItem, UUID> {}
