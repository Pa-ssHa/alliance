package ru.kozelsk.alliance.controllers.admin.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.excursion.booking.Booking;
import ru.kozelsk.alliance.services.excursion.booking.BookingService;
import ru.kozelsk.alliance.services.excursion.booking.UnavailablePeriodService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@IsAdmin
@RestController
@RequestMapping("/admin/booking")
public class BookingTourAdminRestController {

    private final BookingService bookingService;
    private final UnavailablePeriodService unavailablePeriodService;

    @Autowired
    public BookingTourAdminRestController(BookingService bookingService, UnavailablePeriodService unavailablePeriodService) {
        this.bookingService = bookingService;
        this.unavailablePeriodService = unavailablePeriodService;
    }

    @PostMapping("/{id}/changeActive")
    public ResponseEntity<?> changeActive(@PathVariable int id) {

        return bookingService.findOne(id)
                .map(booking -> {
                    booking.setActive(!booking.isActive());
                    bookingService.save(booking);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deleteBooking(@PathVariable int id) {

        if (bookingService.findOne(id).isPresent()) {
            bookingService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/timeslots")
    public List<LocalDateTime> getAvailableTimeSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer bookingId) {

        List<LocalDateTime> slots = bookingService.getAvailableTimeSlots(date);

        if (bookingId != null) {
            Optional<Booking> bookingOpt = bookingService.findOne(bookingId);
            if (bookingOpt.isPresent()) {
                LocalDateTime bookingTime = bookingOpt.get().getBookingTime();
                if (!slots.contains(bookingTime) && bookingTime.toLocalDate().equals(date)) {
                    slots.add(bookingTime);
                    slots.sort(LocalDateTime::compareTo);
                }
            }
        }

        return slots;
    }

    @PostMapping("unavailable/{id}/delete")
    public ResponseEntity<?> deleteBookingUnavailable(@PathVariable int id) {

        if (unavailablePeriodService.findOne(id).isPresent()) {
            unavailablePeriodService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
