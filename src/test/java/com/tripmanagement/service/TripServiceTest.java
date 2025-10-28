package com.tripmanagement.service;
import com.tripmanagement.dto.TripDTO;
import com.tripmanagement.dto.TripSummaryDTO;
import com.tripmanagement.entity.Trip;
import com.tripmanagement.entity.Trip.TripStatus;
import com.tripmanagement.exception.ResourceNotFoundException;
import com.tripmanagement.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {
    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private TripServiceImpl tripService;

    private Trip trip;
    private TripDTO tripDTO;

    @BeforeEach
    void setUp() {
        trip = new Trip();
        trip.setId(1);
        trip.setDestination("Paris");
        trip.setStartDate(LocalDate.of(2025, 9, 10));
        trip.setEndDate(LocalDate.of(2025, 9, 20));
        trip.setPrice(1500.0);
        trip.setStatus(TripStatus.PLANNED);

        tripDTO = new TripDTO();
        tripDTO.setId(1);
        tripDTO.setDestination("Paris");
        tripDTO.setStartDate(LocalDate.of(2025, 9, 10));
        tripDTO.setEndDate(LocalDate.of(2025, 9, 20));
        tripDTO.setPrice(1500.0);
        tripDTO.setStatus(TripStatus.PLANNED);
    }

    @Test
    void testCreateTrip() {
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);

        TripDTO result = tripService.createTrip(tripDTO);

        assertNotNull(result);
        assertEquals("Paris", result.getDestination());
        assertEquals(1500.0, result.getPrice());
        verify(tripRepository, times(1)).save(any(Trip.class));
    }

    @Test
    void testGetAllTrips() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Trip> tripList = Arrays.asList(trip);
        Page<Trip> tripPage = new PageImpl<>(tripList, pageable, tripList.size());

        when(tripRepository.findAll(pageable)).thenReturn(tripPage);

        Page<TripDTO> result = tripService.getAllTrips(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Paris", result.getContent().get(0).getDestination());
        verify(tripRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetTripById() {
        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));

        TripDTO result = tripService.getTripById(1);

        assertNotNull(result);
        assertEquals("Paris", result.getDestination());
        verify(tripRepository, times(1)).findById(1);
    }

    @Test
    void testGetTripByIdNotFound() {
        when(tripRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            tripService.getTripById(999);
        });

        verify(tripRepository, times(1)).findById(999);
    }

    @Test
    void testUpdateTrip() {
        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);

        tripDTO.setDestination("London");
        TripDTO result = tripService.updateTrip(1, tripDTO);

        assertNotNull(result);
        verify(tripRepository, times(1)).findById(1);
        verify(tripRepository, times(1)).save(any(Trip.class));
    }

    @Test
    void testDeleteTrip() {
        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        doNothing().when(tripRepository).delete(trip);

        tripService.deleteTrip(1);

        verify(tripRepository, times(1)).findById(1);
        verify(tripRepository, times(1)).delete(trip);
    }

    @Test
    void testSearchTripsByDestination() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Trip> tripList = Arrays.asList(trip);
        Page<Trip> tripPage = new PageImpl<>(tripList, pageable, tripList.size());

        when(tripRepository.findByDestinationContainingIgnoreCase("Paris", pageable))
                .thenReturn(tripPage);

        Page<TripDTO> result = tripService.searchTripsByDestination("Paris", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(tripRepository, times(1))
                .findByDestinationContainingIgnoreCase("Paris", pageable);
    }

    @Test
    void testFilterTripsByStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Trip> tripList = Arrays.asList(trip);
        Page<Trip> tripPage = new PageImpl<>(tripList, pageable, tripList.size());

        when(tripRepository.findByStatus(TripStatus.PLANNED, pageable)).thenReturn(tripPage);

        Page<TripDTO> result = tripService.filterTripsByStatus(TripStatus.PLANNED, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(tripRepository, times(1)).findByStatus(TripStatus.PLANNED, pageable);
    }

    @Test
    void testGetTripSummary() {
        when(tripRepository.count()).thenReturn(50L);
        when(tripRepository.findMinPrice()).thenReturn(500.0);
        when(tripRepository.findMaxPrice()).thenReturn(4500.0);
        when(tripRepository.findAveragePrice()).thenReturn(2200.0);

        TripSummaryDTO result = tripService.getTripSummary();

        assertNotNull(result);
        assertEquals(50L, result.getTotalTrips());
        assertEquals(500.0, result.getMinPrice());
        assertEquals(4500.0, result.getMaxPrice());
        assertEquals(2200.0, result.getAveragePrice());
    }
}
