package org.example.repository;

import org.example.entity.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
    @Query("SELECT c.city AS city, COUNT(ur) AS count " +
            "FROM UserRequest ur JOIN ur.city c " +
            "WHERE ur.requestedAt >= :since " +
            "GROUP BY c.city " +
            "ORDER BY count DESC")
    List<Object[]> getCityStats(@Param("since") LocalDateTime since);

    @Query("SELECT EXTRACT(HOUR FROM ur.requestedAt) AS hour, COUNT(ur) AS count " +
            "FROM UserRequest ur " +
            "WHERE ur.requestedAt >= :since " +
            "GROUP BY hour " +
            "ORDER BY count DESC")
    List<Object[]> getHourStats(@Param("since") LocalDateTime since);
}
