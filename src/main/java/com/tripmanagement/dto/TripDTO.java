package com.tripmanagement.dto;
import com.tripmanagement.entity.Trip.TripStatus;
import com.tripmanagement.validation.ValidDateRange;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
@ValidDateRange
public class TripDTO {
    private Integer id;

    @NotBlank(message = "Destination cannot be empty")
    private String destination;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Status cannot be null")
    private TripStatus status;

    // Constructors
    public TripDTO() {
    }

    public TripDTO(Integer id, String destination, LocalDate startDate,
                   LocalDate endDate, Double price, TripStatus status) {
        this.id = id;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.status = status;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }
}
