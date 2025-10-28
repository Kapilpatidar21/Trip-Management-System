package com.tripmanagement.repository;
import com.tripmanagement.entity.Trip;
import com.tripmanagement.entity.Trip.TripStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    // Search by destination (case-insensitive, partial match)
    Page<Trip> findByDestinationContainingIgnoreCase(String destination, Pageable pageable);

    // Filter by status
    Page<Trip> findByStatus(TripStatus status, Pageable pageable);

    // Find trips within date range
    @Query("SELECT t FROM Trip t WHERE t.startDate >= :start AND t.endDate <= :end")
    List<Trip> findTripsInDateRange(@Param("start") LocalDate start,
                                    @Param("end") LocalDate end);

    // Get minimum price
    @Query("SELECT MIN(t.price) FROM Trip t")
    Double findMinPrice();

    // Get maximum price
    @Query("SELECT MAX(t.price) FROM Trip t")
    Double findMaxPrice();

    // Get average price
    @Query("SELECT AVG(t.price) FROM Trip t")
    Double findAveragePrice();
}
