package com.tripmanagement.controller;
import com.tripmanagement.dto.TripDTO;
import com.tripmanagement.dto.TripSummaryDTO;
import com.tripmanagement.entity.Trip.TripStatus;
import com.tripmanagement.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
@Tag(name = "Trip Management", description = "APIs for managing trips")
public class TripController {
    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    @Operation(summary = "Create a new trip", description = "Add a new trip to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trip created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<TripDTO> createTrip(@Valid @RequestBody TripDTO tripDTO) {
        TripDTO createdTrip = tripService.createTrip(tripDTO);
        return new ResponseEntity<>(createdTrip, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all trips", description = "Retrieve paginated and sorted list of all trips")
    public ResponseEntity<Page<TripDTO>> getAllTrips(
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field and direction (e.g., price,desc)")
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        Sort.Direction direction = sort.length > 1 && sort[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<TripDTO> trips = tripService.getAllTrips(pageable);
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get trip by ID", description = "Retrieve a specific trip by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip found"),
            @ApiResponse(responseCode = "404", description = "Trip not found")
    })
    public ResponseEntity<TripDTO> getTripById(@PathVariable Integer id) {
        TripDTO trip = tripService.getTripById(id);
        return ResponseEntity.ok(trip);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update trip", description = "Update an existing trip by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip updated successfully"),
            @ApiResponse(responseCode = "404", description = "Trip not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<TripDTO> updateTrip(
            @PathVariable Integer id,
            @Valid @RequestBody TripDTO tripDTO) {
        TripDTO updatedTrip = tripService.updateTrip(id, tripDTO);
        return ResponseEntity.ok(updatedTrip);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete trip", description = "Delete a trip by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trip deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Trip not found")
    })
    public ResponseEntity<Void> deleteTrip(@PathVariable Integer id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search trips", description = "Search trips by destination (partial match)")
    public ResponseEntity<Page<TripDTO>> searchTrips(
            @Parameter(description = "Destination to search for")
            @RequestParam String destination,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TripDTO> trips = tripService.searchTripsByDestination(destination, pageable);
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter trips by status", description = "Get trips filtered by their status")
    public ResponseEntity<Page<TripDTO>> filterTripsByStatus(
            @Parameter(description = "Trip status (PLANNED, ONGOING, COMPLETED)")
            @RequestParam TripStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TripDTO> trips = tripService.filterTripsByStatus(status, pageable);
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/daterange")
    @Operation(summary = "Get trips in date range", description = "Retrieve trips within a specific date range")
    public ResponseEntity<List<TripDTO>> getTripsInDateRange(
            @Parameter(description = "Start date (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @Parameter(description = "End date (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        List<TripDTO> trips = tripService.getTripsInDateRange(start, end);
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/summary")
    @Operation(summary = "Get trip summary", description = "Get statistics summary of all trips")
    public ResponseEntity<TripSummaryDTO> getTripSummary() {
        TripSummaryDTO summary = tripService.getTripSummary();
        return ResponseEntity.ok(summary);
    }
}
