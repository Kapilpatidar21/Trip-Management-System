package com.tripmanagement.validation;
import com.tripmanagement.dto.TripDTO;
import com.tripmanagement.entity.Trip;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDate startDate = null;
        LocalDate endDate = null;

        if (value instanceof Trip) {
            Trip trip = (Trip) value;
            startDate = trip.getStartDate();
            endDate = trip.getEndDate();
        } else if (value instanceof TripDTO) {
            TripDTO tripDTO = (TripDTO) value;
            startDate = tripDTO.getStartDate();
            endDate = tripDTO.getEndDate();
        }

        if (startDate == null || endDate == null) {
            return true;
        }
        return endDate.isAfter(startDate);
    }
}
