package ru.kozelsk.alliance.controllers.admin.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.excursion.booking.Booking;
import ru.kozelsk.alliance.models.excursion.booking.UnavailablePeriod;
import ru.kozelsk.alliance.services.excursion.booking.BookingService;
import ru.kozelsk.alliance.services.excursion.booking.UnavailablePeriodService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@IsAdmin
@Controller
@RequestMapping("/admin/booking")
public class BookingAdminController {

    private final BookingService bookingService;
    private final UnavailablePeriodService unavailablePeriodService;

    @Autowired
    public BookingAdminController(BookingService bookingService, UnavailablePeriodService unavailablePeriodService) {
        this.bookingService = bookingService;
        this.unavailablePeriodService = unavailablePeriodService;
    }

    @GetMapping
    public String allBookings(Model model) {
        List<Booking> bookings = bookingService.findAll();
        Collections.sort(bookings);
        model.addAttribute("bookings", bookings);
        model.addAttribute("activeCount", bookings.stream().filter(Booking::isActive).count());

        return "admin/booking/main";
    }


    @GetMapping("/edit/{id}")
    public String editBooking(@PathVariable int id,
                              @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              @RequestParam(value = "description", required = false) String description,
                              @RequestParam(value = "scrollPosition", required = false) Double scrollPosition,
                              Model model){

        Booking booking = bookingService.findOne(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking Id:" + id));

        if (description != null) {
            booking.setDescription(description);
        }

        model.addAttribute("booking", booking);

        List<LocalDateTime> availableSlots = new ArrayList<>();
        if (date != null) {
            availableSlots = bookingService.getAvailableTimeSlots(date);
            model.addAttribute("availableSlots", availableSlots);
        }
        if(date == null || availableSlots.isEmpty()) {
            model.addAttribute("noSlotsAvailable", true);
        }

        model.addAttribute("selectedDate", date);
        return "admin/booking/edit";
    }


    @PostMapping("/edit/{id}")
    public String updateBooking(@PathVariable int id,
                                @RequestParam(value = "bookingTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime bookingTime,
                                @RequestParam(value = "description", required = false) String description,
                                Model model) {

        try {
            Booking booking = bookingService.findOne(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid booking Id:" + id));
            if(bookingTime!=null) {
                booking.setBookingTime(bookingTime);
            }
            booking.setDescription(description);

            bookingService.save(booking);

            return "redirect:/admin/booking";
        } catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "admin/booking/edit";
        }

    }

    @GetMapping("/unavailable")
    public String allBookingsUnavailable(Model model) {
//        List<UnavailablePeriod> unavailablePeriods = unavailablePeriodService.findAll();
//        Collections.sort(unavailablePeriods);
//        model.addAttribute("unavailablePeriods", unavailablePeriods);
        model.addAttribute("unavailablePeriods", unavailablePeriodService.findUnavailablePeriodsByDay());

        return "admin/booking/unavailable/main";
    }

    @GetMapping("unavailable/add")
    public String addUnavailableSlot(
                              @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              @RequestParam(value = "scrollPosition", required = false) Double scrollPosition,
                              Model model){

        model.addAttribute("selectedDate", date);

        List<LocalDateTime> availableSlots = new ArrayList<>();
        if (date != null) {
            availableSlots = bookingService.getAvailableTimeSlots(date);
            model.addAttribute("availableSlots", availableSlots);
        }
        if(date == null || availableSlots.isEmpty()) {
            model.addAttribute("noSlotsAvailable", true);
        }

        return "admin/booking/unavailable/addSlots";
    }

    @PostMapping("/unavailable/add")
    public String addBookingUnavailable(
                                @RequestParam(value = "unavailableTimeList", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) List<LocalDateTime> unavailableTimeList,
                                Model model) {

        try {
            unavailablePeriodService.addUnavailablePeriod(unavailableTimeList);
            return "redirect:/admin/booking/unavailable";

        } catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "admin/booking/unavailable/addSlots";
        }

    }

}