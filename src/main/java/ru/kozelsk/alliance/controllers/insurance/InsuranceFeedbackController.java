package ru.kozelsk.alliance.controllers.insurance;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.insurance.FeedbackInsurance;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.insurance.FeedbackInsuranceService;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;
import ru.kozelsk.alliance.utils.services.UserFromPrincipal;

import java.security.Principal;
import java.util.Date;

@Controller
@RequestMapping("insurance/feedback")
public class InsuranceFeedbackController {

    private final MyUserDetailsService myUserDetailsService;
    private final UserFromPrincipal userFromPrincipal;
    private final FeedbackInsuranceService feedbackInsuranceService;

    @Autowired
    public InsuranceFeedbackController(MyUserDetailsService myUserDetailsService, UserFromPrincipal userFromPrincipal, FeedbackInsuranceService feedbackInsuranceService) {
        this.myUserDetailsService = myUserDetailsService;
        this.userFromPrincipal = userFromPrincipal;
        this.feedbackInsuranceService = feedbackInsuranceService;
    }

    @PostMapping()
    public String createFeedback(@ModelAttribute("newFeedbackInsurance")FeedbackInsurance feedbackInsurance,
                                 Principal principal, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "insurance/main";
        }

        User user = userFromPrincipal.getUser(principal);
        if (user == null) {
            return "redirect:/insurance";
        }

        user.setInsuranceFeedback(true);
        myUserDetailsService.save(user);

        feedbackInsurance.setUser(user);
        feedbackInsurance.setDateOfPlacement(new Date());
        feedbackInsurance.setActive(false);
        feedbackInsuranceService.save(feedbackInsurance);

        return "redirect:/insurance";
    }

    @IsAdmin
    @PostMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable int id) {

        User user = feedbackInsuranceService.findOne(id).get().getUser();
        user.setInsuranceFeedback(false);
        myUserDetailsService.save(user);
        feedbackInsuranceService.delete(id);

        return "redirect:/insurance";
    }
}
