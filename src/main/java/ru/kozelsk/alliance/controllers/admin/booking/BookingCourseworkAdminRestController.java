package ru.kozelsk.alliance.controllers.admin.booking;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.services.coursework.BookingCourseworkService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;

@IsAdmin
@RestController
@RequestMapping("/admin/booking-coursework")
public class BookingCourseworkAdminRestController {

    private final BookingCourseworkService bookingCourseworkService;

    @Autowired
    public BookingCourseworkAdminRestController(BookingCourseworkService bookingCourseworkService) {
        this.bookingCourseworkService = bookingCourseworkService;
    }

    @PostMapping("/changeActive/{id}")
    public ResponseEntity<?> changeActive(@PathVariable("id") int id){
        return bookingCourseworkService.findOne(id)
                .map(bookingCoursework -> {
                    bookingCoursework.setActive(!bookingCoursework.isActive());
                    bookingCourseworkService.save(bookingCoursework);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteBookingCoursework(@PathVariable("id") int id){
        return bookingCourseworkService.findOne(id)
                .map(bookingCoursework -> {
                    bookingCourseworkService.delete(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
