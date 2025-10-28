package com.tripmanagement.dto;

public class TripSummaryDTO {
    private Long totalTrips;
    private Double minPrice;
    private Double maxPrice;
    private Double averagePrice;

    // Constructors
    public TripSummaryDTO() {
    }

    public TripSummaryDTO(Long totalTrips, Double minPrice,
                          Double maxPrice, Double averagePrice) {
        this.totalTrips = totalTrips;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.averagePrice = averagePrice;
    }

    // Getters and Setters
    public Long getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(Long totalTrips) {
        this.totalTrips = totalTrips;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
    }
}
