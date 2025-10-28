package com.tripmanagement.service;
import com.tripmanagement.dto.TripDTO;
import com.tripmanagement.dto.TripSummaryDTO;
import com.tripmanagement.entity.Trip;
import com.tripmanagement.entity.Trip.TripStatus;
import com.tripmanagement.exception.InvalidDateRangeException;
import com.tripmanagement.exception.ResourceNotFoundException;
import com.tripmanagement.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
public class TripServiceImpl implements TripService {
    private final TripRepository tripRepository;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public TripDTO createTrip(TripDTO tripDTO) {
        Trip trip = mapToEntity(tripDTO);
        Trip savedTrip = tripRepository.save(trip);
        return mapToDTO(savedTrip);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TripDTO> getAllTrips(Pageable pageable) {
        Page<Trip> trips = tripRepository.findAll(pageable);
        return trips.map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TripDTO getTripById(Integer id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + id));
        return mapToDTO(trip);
    }

    @Override
    public TripDTO updateTrip(Integer id, TripDTO tripDTO) {
        Trip existingTrip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + id));

        existingTrip.setDestination(tripDTO.getDestination());
        existingTrip.setStartDate(tripDTO.getStartDate());
        existingTrip.setEndDate(tripDTO.getEndDate());
        existingTrip.setPrice(tripDTO.getPrice());
        existingTrip.setStatus(tripDTO.getStatus());

        Trip updatedTrip = tripRepository.save(existingTrip);
        return mapToDTO(updatedTrip);
    }
    @Override
    public void deleteTrip(Integer id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + id));
        tripRepository.delete(trip);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TripDTO> searchTripsByDestination(String destination, Pageable pageable) {
        Page<Trip> trips = tripRepository.findByDestinationContainingIgnoreCase(destination, pageable);
        return trips.map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TripDTO> filterTripsByStatus(TripStatus status, Pageable pageable) {
        Page<Trip> trips = tripRepository.findByStatus(status, pageable);
        return trips.map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripDTO> getTripsInDateRange(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new InvalidDateRangeException("End date must be after start date");
        }

        List<Trip> trips = tripRepository.findTripsInDateRange(start, end);
        return trips.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TripSummaryDTO getTripSummary() {
        long totalTrips = tripRepository.count();
        Double minPrice = tripRepository.findMinPrice();
        Double maxPrice = tripRepository.findMaxPrice();
        Double averagePrice = tripRepository.findAveragePrice();

        return new TripSummaryDTO(
                totalTrips,
                minPrice != null ? minPrice : 0.0,
                maxPrice != null ? maxPrice : 0.0,
                averagePrice != null ? averagePrice : 0.0
        );
    }

    // Helper methods to convert between Entity and DTO
    private TripDTO mapToDTO(Trip trip) {
        return new TripDTO(
                trip.getId(),
                trip.getDestination(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getPrice(),
                trip.getStatus()
        );
    }

    private Trip mapToEntity(TripDTO tripDTO) {
        Trip trip = new Trip();
        trip.setDestination(tripDTO.getDestination());
        trip.setStartDate(tripDTO.getStartDate());
        trip.setEndDate(tripDTO.getEndDate());
        trip.setPrice(tripDTO.getPrice());
        trip.setStatus(tripDTO.getStatus());
        return trip;
    }
}
