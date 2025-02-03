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
import java.util.Optional;

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

    // Проверка, есть ли у человека бронирование по конкретному туру.
    // Возвращает true, если у человека есть еще не истекшее бронирование
    public boolean isBookingCurrentTourForUser(int tourId, int userId){
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = getBookingForUser(userId);
        for (Booking booking : bookings) {
            if(booking.getTour().getId() == tourId && booking.getBookingTime().isAfter(now)){
                return true;
            }
        }
        return false;
    }

    // проверяет есть ли по этому туру у человека бронь и выдает последнюю
    // список сортируется, чтобы избежать проверки старых бронирований
    public Optional<Booking> isBookingForUser(List<Booking> bookings, int userId){
        bookings.sort((b1,  b2) -> b2.getBookingTime().compareTo(b1.getBookingTime()));

        for (Booking booking : bookings) {
            if(booking.getUser().getId() == userId){
                return Optional.of(booking);
            }
        }
        return Optional.empty();
    }

    // добавить сюда кэширование
    public boolean isTimeSlotAvailable(LocalDateTime bookingTime){
        LocalDateTime start = bookingTime.minusHours(1);
        LocalDateTime end = bookingTime.plusHours(1);
        List<Booking> bookings = bookingRepository.findByBookingTimeBetween(start, end);
        return bookings.isEmpty();
    }

    // создание бронирования
    public Booking createBooking(Tour tour, User user, LocalDateTime bookingTime){

        if(isBookingCurrentTourForUser(tour.getId(), user.getId())){
            throw new IllegalStateException("У вас уже есть активное бронирование этого тура");
        }

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

    // выводить занятые слоты (пока не используется)
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
