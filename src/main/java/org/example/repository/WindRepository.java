package org.example.repository;

import org.example.entity.Wind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WindRepository extends JpaRepository<Wind, UUID> {

    Wind findByScheduleId(Long id);
}
