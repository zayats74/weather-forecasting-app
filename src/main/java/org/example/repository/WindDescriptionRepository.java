package org.example.repository;


import org.example.entity.WindDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WindDescriptionRepository extends JpaRepository<WindDescription, Long> {
    @Query("SELECT wd FROM WindDescription wd WHERE " +
            "(wd.degreeStart <= wd.degreeEnd AND :degrees BETWEEN wd.degreeStart AND wd.degreeEnd) OR " +
            "(wd.degreeStart > wd.degreeEnd AND (:degrees >= wd.degreeStart OR :degrees <= wd.degreeEnd))")
    WindDescription findByDegree(@Param("degrees") double degrees);

    List<WindDescription> findAllByOrderByDegreeStartAsc();
}
