package ru.kozelsk.alliance.controllers.coursework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kozelsk.alliance.models.coursework.FeedbackCoursework;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.coursework.FeedbackCourseworkService;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;
import ru.kozelsk.alliance.utils.services.UserFromPrincipal;

import java.security.Principal;
import java.util.Date;

@Controller
@RequestMapping("/coursework/feedback")
public class FeedbackCourseworkController {

    private final FeedbackCourseworkService feedbackCourseworkService;
    private final MyUserDetailsService myUserDetailsService;
    private final UserFromPrincipal userFromPrincipal;

    @Autowired
    public FeedbackCourseworkController(FeedbackCourseworkService feedbackCourseworkService, MyUserDetailsService myUserDetailsService, UserFromPrincipal userFromPrincipal) {
        this.feedbackCourseworkService = feedbackCourseworkService;
        this.myUserDetailsService = myUserDetailsService;
        this.userFromPrincipal = userFromPrincipal;
    }

    @PostMapping()
    public String createFeedback(@ModelAttribute("newFeedbackCoursework") FeedbackCoursework feedbackCoursework,
                                 Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect: /coursework";
        }

        User user = userFromPrincipal.getUser(principal);
        if (user == null) {
            return "redirect:/coursework";
        }

        user.setCourseworkFeedback(true);
        myUserDetailsService.save(user);

        feedbackCoursework.setUser(user);
        feedbackCoursework.setDateOfPlacement(new Date());
        feedbackCoursework.setActive(false);
        feedbackCourseworkService.save(feedbackCoursework);

        return "redirect:/coursework";
    }

    @IsAdmin
    @PostMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable int id) {

        User user = feedbackCourseworkService.findOne(id).get().getUser();
        user.setCourseworkFeedback(false);
        myUserDetailsService.save(user);

        feedbackCourseworkService.delete(id);
        return "redirect:/coursework";
    }
}
