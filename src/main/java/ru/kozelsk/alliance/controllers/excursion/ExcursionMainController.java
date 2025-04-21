package ru.kozelsk.alliance.controllers.excursion;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kozelsk.alliance.models.excursion.FeedbackExcursion;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.excursion.FeedbackExcursionService;
import ru.kozelsk.alliance.services.excursion.TourImageService;
import ru.kozelsk.alliance.services.excursion.TourService;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;
import ru.kozelsk.alliance.utils.services.CheckAuthorizeService;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/excursion")
public class ExcursionMainController {

    private final TourService tourService;
    private final CheckAuthorizeService checkAuthorizeService;
    private final FeedbackExcursionService feedbackExcursionService;
    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public ExcursionMainController(TourService tourService, CheckAuthorizeService checkAuthorizeService, FeedbackExcursionService feedbackExcursionService, MyUserDetailsService myUserDetailsService) {
        this.tourService = tourService;
        this.checkAuthorizeService = checkAuthorizeService;
        this.feedbackExcursionService = feedbackExcursionService;
        this.myUserDetailsService = myUserDetailsService;
    }

    @GetMapping()
    public String allTour(Model model, @AuthenticationPrincipal OAuth2User oAuth2User,  Principal principal) {
        model.addAttribute("allTour", tourService.findAll());
        model.addAttribute("feedbackExcursions", feedbackExcursionService.findAll().stream().filter(FeedbackExcursion::isActive));

        if(oAuth2User != null) {
            Optional<User> user = myUserDetailsService.findByEmail(oAuth2User.getAttribute("email"));
            model.addAttribute("hasFeedback", user.get().isExcursionFeedback());
        } else if (principal != null) {
            Optional<User> user = myUserDetailsService.findByEmail(principal.getName());
            model.addAttribute("hasFeedback", user.get().isExcursionFeedback());
        }

        model.addAttribute("checkAdmin", checkAuthorizeService.checkAdmin(principal));
        model.addAttribute("newFeedbackExcursion", new FeedbackExcursion());

        return "excursion/main";
    }
}
