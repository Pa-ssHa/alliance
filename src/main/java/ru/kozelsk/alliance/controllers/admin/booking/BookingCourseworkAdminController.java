package ru.kozelsk.alliance.controllers.admin.booking;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.coursework.BookingCoursework;
import ru.kozelsk.alliance.services.coursework.BookingCourseworkService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;

import java.util.Collections;
import java.util.List;

@IsAdmin
@Controller
@RequestMapping("/admin/booking-coursework")
public class BookingCourseworkAdminController {

    private final BookingCourseworkService bookingCourseworkService;

    @Autowired
    public BookingCourseworkAdminController(BookingCourseworkService bookingCourseworkService) {
        this.bookingCourseworkService = bookingCourseworkService;
    }

    @GetMapping
    public String allBookingCoursework(Model model) {
        List<BookingCoursework> bookingCoursework = bookingCourseworkService.findAll();
        Collections.sort(bookingCoursework);
        model.addAttribute("bookingCoursework", bookingCoursework);
        model.addAttribute("activeCount", bookingCourseworkService.findAll().stream().filter(BookingCoursework::isActive).count());
        return "admin/booking-coursework/main";
    }

    @GetMapping("/edit/{id}")
    public String editCoursework(@PathVariable("id") int id, Model model) {
        model.addAttribute("upBookingCoursework", bookingCourseworkService.findOne(id));
        return "admin/booking-coursework/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCoursework(@PathVariable("id") int id,
                                   @RequestParam(value = "description", required = false) String description, Model model) {

        try {
            BookingCoursework bookingCoursework = bookingCourseworkService.findOne(id)
                    .orElseThrow(() -> new RuntimeException("Coursework not found"));
            if (description != null) {
                bookingCoursework.setDescription(description);
            }
            bookingCourseworkService.save(bookingCoursework);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/admin/booking-coursework";
    }
}
