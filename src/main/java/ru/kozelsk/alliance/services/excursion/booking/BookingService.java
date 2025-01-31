package ru.kozelsk.alliance.services.excursion.booking;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.excursion.Tour;
import ru.kozelsk.alliance.models.excursion.booking.Booking;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.repositories.excursion.booking.BookingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getBookingForTour(int tourId){
        return bookingRepository.findByTourId(tourId);
    }

    public List<Booking> getBookingForUser(int userId){
        return bookingRepository.findByUserId(userId);
    }

    public boolean isTimeSlotAvailable(LocalDateTime bookingTime){
        LocalDateTime start = bookingTime.minusHours(1);
        LocalDateTime end = bookingTime.plusHours(1);
        List<Booking> bookings = bookingRepository.findByBookingTimeBetween(start, end);
        return bookings.isEmpty();
    }

    public Booking createBooking(Tour tour, User user, LocalDateTime bookingTime){

        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(17, 30);

        if(bookingTime.toLocalTime().isBefore(startTime) || bookingTime.toLocalTime().isAfter(endTime)){
            throw new IllegalArgumentException("Бронирование возможно с 10 до 17.30");
        }

        if(!isTimeSlotAvailable(bookingTime)){
            throw new IllegalArgumentException("выбранное время уже занято");
        }

        Booking booking = new Booking();
        booking.setTour(tour);
        booking.setUser(user);
        booking.setBookingTime(bookingTime);
        return bookingRepository.save(booking);
    }

    public List<LocalDateTime> getBusyTimeSlots(int tourId){
        List<LocalDateTime> busyTimeslots = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findByTourId(tourId);
        for(Booking booking : bookings){
            busyTimeslots.add(booking.getBookingTime());
        }
        return busyTimeslots;
    }

    public List<LocalDateTime> getAvailableTimeSlots(LocalDate date){
        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalDateTime startTime = date.atTime(10,0);
        LocalDateTime endTime = date.atTime(17,30);

        while(startTime.isBefore(endTime)){
            if(isTimeSlotAvailable(startTime)) {
                availableSlots.add(startTime);
            }
            startTime = startTime.plusHours(1).plusMinutes(30);
        }

        return availableSlots;
    }
}
