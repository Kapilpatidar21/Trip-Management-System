package com.tripmanagement.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripmanagement.dto.TripDTO;
import com.tripmanagement.entity.Trip.TripStatus;
import com.tripmanagement.service.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TripController.class)

public class TripControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TripService tripService;

    private TripDTO tripDTO;

    @BeforeEach
    void setUp() {
        tripDTO = new TripDTO();
        tripDTO.setId(1);
        tripDTO.setDestination("Paris");
        tripDTO.setStartDate(LocalDate.of(2025, 9, 10));
        tripDTO.setEndDate(LocalDate.of(2025, 9, 20));
        tripDTO.setPrice(1500.0);
        tripDTO.setStatus(TripStatus.PLANNED);
    }

    @Test
    void testCreateTrip() throws Exception {
        when(tripService.createTrip(any(TripDTO.class))).thenReturn(tripDTO);

        mockMvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.destination").value("Paris"))
                .andExpect(jsonPath("$.price").value(1500.0));
    }

    @Test
    void testGetAllTrips() throws Exception {
        List<TripDTO> tripList = Arrays.asList(tripDTO);
        Page<TripDTO> tripPage = new PageImpl<>(tripList);

        when(tripService.getAllTrips(any())).thenReturn(tripPage);

        mockMvc.perform(get("/api/trips")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].destination").value("Paris"));
    }

    @Test
    void testGetTripById() throws Exception {
        when(tripService.getTripById(1)).thenReturn(tripDTO);

        mockMvc.perform(get("/api/trips/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destination").value("Paris"))
                .andExpect(jsonPath("$.price").value(1500.0));
    }

    @Test
    void testUpdateTrip() throws Exception {
        when(tripService.updateTrip(eq(1), any(TripDTO.class))).thenReturn(tripDTO);

        mockMvc.perform(put("/api/trips/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destination").value("Paris"));
    }

    @Test
    void testDeleteTrip() throws Exception {
        mockMvc.perform(delete("/api/trips/1"))
                .andExpect(status().isNoContent());
    }
}
