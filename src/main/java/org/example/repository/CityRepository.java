package org.example.repository;

import org.example.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface CityRepository extends JpaRepository<City, UUID> {

    boolean existsByCity(String city);

    City findByCity(String city);
}
