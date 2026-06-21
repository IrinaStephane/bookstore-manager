package com.hei.school.repository;

import com.hei.school.entity.Arrival;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrivalRepository extends JpaRepository<Arrival, UUID> {}
