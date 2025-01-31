package ru.kozelsk.alliance.controllers.excursion;

import jakarta.persistence.metamodel.ListAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/excursion/booking")
@Controller
public class BookingController {

    private final BookingService bookingService;
    private final TourService tourService;

    @Autowired
    public BookingController(BookingService bookingService, TourService tourService) {
        this.bookingService = bookingService;
        this.tourService = tourService;
    }

    @GetMapping("/{tourId}")
    public String showBookingForm(@PathVariable int tourId,
                                  @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                  Model model) {
        Tour tour = tourService.findOne(tourId);
        model.addAttribute("tour", tour);

        /*if(date != null) {
            //получаем доступные верменные слоты
            List<LocalDateTime> availableSlots = bookingService.getAvailableTimeSlots(tourId, date);

            LocalDateTime now = LocalDateTime.now();
            List<LocalDateTime> futureSlots = availableSlots.stream()
                            .filter(slot -> slot.isAfter(now))
                            .collect(Collectors.toList());

            model.addAttribute("availableSlots", futureSlots);
            model.addAttribute("noSlotsAvailable", futureSlots.isEmpty());
        }*/

        if(date != null) {
            List<LocalDateTime> availableSlots = bookingService.getAvailableTimeSlots(date);
            model.addAttribute("availableSlots", availableSlots);
        }

        model.addAttribute("selectedDate", date);
        return "excursion/booking/form";
    }

    @PostMapping("/{tourId}")
    public String bookTour(@PathVariable int tourId,
                           @RequestParam LocalDateTime bookingTime,
                           @AuthenticationPrincipal User user,
                           Model model) {
        Tour tour = tourService.findOne(tourId);

        // проверка времени, прошло оно или нет
        if(bookingTime.isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Выбранное время уже прошло, его нельзя выбрать");
            model.addAttribute("tour", tour);
            return "excursion/booking/form";
        }

        if(bookingService.isTimeSlotAvailable(bookingTime)) {
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
