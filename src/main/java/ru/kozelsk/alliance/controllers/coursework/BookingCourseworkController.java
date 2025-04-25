package ru.kozelsk.alliance.controllers.coursework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.coursework.BookingCoursework;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.coursework.BookingCourseworkService;
import ru.kozelsk.alliance.services.coursework.CourseworkService;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;
import ru.kozelsk.alliance.utils.services.UserFromPrincipal;

import java.security.Principal;

@Controller
@RequestMapping("/coursework")
public class BookingCourseworkController {

    private final BookingCourseworkService bookingCourseworkService;
    private final UserFromPrincipal userFromPrincipal;
    private final CourseworkService courseworkService;
    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public BookingCourseworkController(BookingCourseworkService bookingCourseworkService, UserFromPrincipal userFromPrincipal, CourseworkService courseworkService, MyUserDetailsService myUserDetailsService) {
        this.bookingCourseworkService = bookingCourseworkService;
        this.userFromPrincipal = userFromPrincipal;
        this.courseworkService = courseworkService;
        this.myUserDetailsService = myUserDetailsService;
    }

    @PostMapping("/booking/{id}")
    public String bookingCoursework(@PathVariable("id") int id, Principal principal,
                                    @RequestParam("phone") String phone,
                                    Model model) {

        User user = userFromPrincipal.getUser(principal);
        if (bookingCourseworkService.isHasBookingForUser(user.getId())){
            model.addAttribute("message", "У вас уже есть запрос на одну из работ, подождите пока с вами свяжутся");
            return "redirect:/coursework/" + id;
        }

        user.setPhone(phone);
        myUserDetailsService.save(user);

        BookingCoursework bookingCoursework = new BookingCoursework();
        bookingCoursework.setUser(user);
        bookingCoursework.setCoursework(courseworkService.findOne(id).get());
        bookingCourseworkService.save(bookingCoursework);

        return "redirect:/coursework/" + id;
    }
}
