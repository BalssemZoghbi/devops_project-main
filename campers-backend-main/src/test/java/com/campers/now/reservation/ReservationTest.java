package com.campers.now.reservation;
   
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.campers.now.models.Reservation;
import com.campers.now.repositories.ReservationRepository;
import com.campers.now.services.Impl.ReservationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

public class ReservationTest {
 
        private ReservationServiceImpl reservationService;
        private ReservationRepository reservationRepository;
    
        @BeforeEach
        void setUp() {
            reservationRepository = mock(ReservationRepository.class);
            reservationService = new ReservationServiceImpl(reservationRepository);
        }
    
        @Test
        void testGetReservationStatisticsByMonth() {
            // Mock data
            List<Object[]> statistics = new ArrayList<>();
            statistics.add(new Object[]{1, 10}); // Month 1 has 10 reservations
            statistics.add(new Object[]{2, 15}); // Month 2 has 15 reservations
    
            // Mock the repository behavior
            when(reservationRepository.getReservationCountByMonth()).thenReturn(statistics);
    
            // Call the method to test
            ResponseEntity<List<Object[]>> result = reservationService.getReservationStatisticsByMonth();
    
            // Verify the result
            assertEquals(statistics, result.getBody());
        }
    @Test
    void testDeactivateOldReservations() {
        // Mock data
        List<Reservation> reservations = new ArrayList<>();
        Reservation activeReservation1 = new Reservation();
        activeReservation1.setDateStart(new Date()); // Current date
        activeReservation1.setDateEnd(new Date(System.currentTimeMillis() + 86400000)); // Tomorrow
        activeReservation1.setActive(true);
        reservations.add(activeReservation1);

        Reservation activeReservation2 = new Reservation();
        activeReservation2.setDateStart(new Date(System.currentTimeMillis() - 86400000)); // Yesterday
        activeReservation2.setDateEnd(new Date()); // Current date
        activeReservation2.setActive(true);
        reservations.add(activeReservation2);

        // Mock the repository behavior
        when(reservationRepository.findAll()).thenReturn(reservations);

        // Call the method to test
        reservationService.deactivateOldReservations();

        // Verify that the deactivateOldReservations method is called with the correct date
        verify(reservationRepository).deactivateOldReservations(any(Date.class));
    }
    @Test
    void testCalculateTotalNumberOfDays() {
        // Mock data
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation1 = new Reservation();
        reservation1.setDateStart(new Date(2024, 5, 1)); // May 1st, 2024
        reservation1.setDateEnd(new Date(2024, 5, 5));   // May 5th, 2024
        reservations.add(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setDateStart(new Date(2024, 5, 10));  // May 10th, 2024
        reservation2.setDateEnd(new Date(2024, 5, 15));    // May 15th, 2024
        reservations.add(reservation2);

        // Mock the repository behavior
        when(reservationRepository.findAll()).thenReturn(reservations);

        // Call the method to test
        int totalDays = reservationService.calculateTotalNumberOfDays(reservations);

        // Verify the result
        assertEquals(9, totalDays);
    }

    }
    