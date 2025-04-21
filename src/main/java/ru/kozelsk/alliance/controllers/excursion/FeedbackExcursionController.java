package ru.kozelsk.alliance.controllers.excursion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kozelsk.alliance.models.excursion.FeedbackExcursion;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.excursion.FeedbackExcursionService;
import ru.kozelsk.alliance.services.insurance.FeedbackInsuranceService;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;
import ru.kozelsk.alliance.utils.services.UserFromPrincipal;

import java.security.Principal;
import java.util.Date;

@Controller
@RequestMapping("/excursion/feedback")
public class FeedbackExcursionController {

    private final MyUserDetailsService myUserDetailsService;
    private final FeedbackExcursionService feedbackExcursionService;
    private final UserFromPrincipal userFromPrincipal;

    @Autowired
    public FeedbackExcursionController(MyUserDetailsService myUserDetailsService, FeedbackExcursionService feedbackExcursionService, UserFromPrincipal userFromPrincipal) {
        this.myUserDetailsService = myUserDetailsService;
        this.feedbackExcursionService = feedbackExcursionService;
        this.userFromPrincipal = userFromPrincipal;
    }

    @PostMapping
    public String createFeedback(@ModelAttribute("newFeedbackExcursion") FeedbackExcursion feedbackExcursion,
                                 Principal principal, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/excursion";
        }

        User user = userFromPrincipal.getUser(principal);
        if (user == null) {
            return "redirect:/excursion";
        }

        user.setExcursionFeedback(true);
        myUserDetailsService.save(user);

        feedbackExcursion.setUser(user);
        feedbackExcursion.setDateOfPlacement(new Date());
        feedbackExcursion.setActive(false);
        feedbackExcursionService.save(feedbackExcursion);

        return "redirect:/excursion";
    }

    @IsAdmin
    @PostMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable int id) {

        User user = feedbackExcursionService.findOne(id).get().getUser();
        user.setExcursionFeedback(false);
        myUserDetailsService.save(user);

        feedbackExcursionService.delete(id);
        return "redirect:/excursion";
    }

}
