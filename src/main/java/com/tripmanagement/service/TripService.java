package com.tripmanagement.service;
import com.tripmanagement.dto.TripDTO;
import com.tripmanagement.dto.TripSummaryDTO;
import com.tripmanagement.entity.Trip.TripStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
public interface TripService {
    TripDTO createTrip(TripDTO tripDTO);

    Page<TripDTO> getAllTrips(Pageable pageable);

    TripDTO getTripById(Integer id);

    TripDTO updateTrip(Integer id, TripDTO tripDTO);

    void deleteTrip(Integer id);

    Page<TripDTO> searchTripsByDestination(String destination, Pageable pageable);

    Page<TripDTO> filterTripsByStatus(TripStatus status, Pageable pageable);

    List<TripDTO> getTripsInDateRange(LocalDate start, LocalDate end);

    TripSummaryDTO getTripSummary();
}
