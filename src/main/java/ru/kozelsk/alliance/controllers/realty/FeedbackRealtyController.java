package ru.kozelsk.alliance.controllers.realty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.realty.FeedbackRealty;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.realty.FeedbackRealtyService;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/realty/feedback")
public class FeedbackRealtyController {

    private final FeedbackRealtyService feedbackRealtyService;
    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public FeedbackRealtyController(FeedbackRealtyService feedbackRealtyService, MyUserDetailsService myUserDetailsService) {
        this.feedbackRealtyService = feedbackRealtyService;
        this.myUserDetailsService = myUserDetailsService;
    }

    @PostMapping()
    public String createFeedbackRealty(@ModelAttribute("newFeedbackRealty") FeedbackRealty feedbackRealty,
                                       Principal principal,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/realty";
        }

        // получаем текущего пользователя
        User user = (User) ((Authentication) principal).getPrincipal();
        user.setRealtyFeedback(true);
        myUserDetailsService.save(user);

        feedbackRealty.setUser(user);
        // записываем текущее время
        feedbackRealty.setDateOfPlacement(new Date());
        feedbackRealtyService.save(feedbackRealty);

        return "redirect:/realty";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable int id) {

        User user = feedbackRealtyService.findOne(id).getUser();
        user.setRealtyFeedback(false);
        myUserDetailsService.save(user);

        feedbackRealtyService.delete(id);
        return "redirect:/realty";
    }
}
