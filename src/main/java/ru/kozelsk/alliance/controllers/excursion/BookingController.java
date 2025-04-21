package ru.kozelsk.alliance.controllers.excursion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.excursion.Tour;
import ru.kozelsk.alliance.models.excursion.booking.Booking;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.excursion.TourService;
import ru.kozelsk.alliance.services.excursion.booking.BookingService;
import ru.kozelsk.alliance.utils.services.UserFromPrincipal;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequestMapping("/excursion/booking")
@Controller
public class BookingController {

    private final BookingService bookingService;
    private final TourService tourService;
    private final UserFromPrincipal userFromPrincipal;

    @Autowired
    public BookingController(BookingService bookingService, TourService tourService, UserFromPrincipal userFromPrincipal) {
        this.bookingService = bookingService;
        this.tourService = tourService;
        this.userFromPrincipal = userFromPrincipal;
    }

    @GetMapping("/{tourId}")
    public String showBookingForm(@PathVariable int tourId,
                                  @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                  Model model) {
        Tour tour = tourService.findOne(tourId);
        model.addAttribute("tour", tour);

        if(date != null) {
            List<LocalDateTime> availableSlots = bookingService.getAvailableTimeSlots(date);
            if (!availableSlots.isEmpty()) {
                model.addAttribute("availableSlots", availableSlots);
            } else {
                model.addAttribute("noSlotsAvailable", true);
            }
        } else {
            model.addAttribute("error", "Не выбрана дата бронирования");
        }

        model.addAttribute("selectedDate", date);
        return "excursion/booking/form";
    }

    @PostMapping("/{tourId}")
    public String bookTour(@PathVariable int tourId,
                           @RequestParam LocalDateTime bookingTime,
                           @RequestParam(value = "phoneNumber") String phoneNumber,
                           Principal principal,
                           Model model) {
        Tour tour = tourService.findOne(tourId);

        // проверка времени, прошло оно или нет
        if(bookingTime.isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Выбранное время уже прошло, его нельзя выбрать");
            model.addAttribute("tour", tour);
            return "excursion/booking/form";
        }

        List<Booking> bookings = bookingService.getBookingForTour(tourId);
        Optional<Booking> booking = bookingService.isBookingForUser(bookings, userFromPrincipal.getUserId(principal))
                .filter(b -> b.getBookingTime().isAfter(LocalDateTime.now()));
        if (booking.isPresent()) {
            model.addAttribute("error", "у вас есть не истекшее бронирование");
        }

        // проверка наличия свободного времени
        if(bookingService.isTimeSlotAvailable(bookingTime)) {
            User user = userFromPrincipal.getUser(principal);
            user.setPhone(phoneNumber);
            bookingService.createBooking(tour, user, bookingTime);
            return "redirect:/excursion/tour/" + tourId;
        } else {
            model.addAttribute("error", "Выбранное время уже занято");
            model.addAttribute("tour", tour);
            return "excursion/booking/form";
        }
    }

    /*@GetMapping("/{tourId}/slots")
    public String showAvailableSlots(@PathVariable int tourId,
                                     @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                     Model model) {
        Tour tour = tourService.findOne(tourId);
        model.addAttribute("tour", tour);

        if(date != null) {
            List<LocalDateTime> availableSlots = bookingService.getAvailableTimeSlots(tourId, date);
            model.addAttribute("availableSlots", availableSlots);
            model.addAttribute("selectionDate", date);
        }
        return "excursion/booking/slots";
    }*/

}
