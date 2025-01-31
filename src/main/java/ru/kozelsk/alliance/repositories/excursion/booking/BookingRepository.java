package ru.kozelsk.alliance.repositories.excursion.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.excursion.booking.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByTourId(int tourId);
    List<Booking> findByUserId(int userId);
    List<Booking> findByBookingTimeBetween(LocalDateTime start, LocalDateTime end);
}
