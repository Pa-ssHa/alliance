package ru.kozelsk.alliance.controllers.coursework;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kozelsk.alliance.models.coursework.FeedbackCoursework;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.coursework.CourseworkService;
import ru.kozelsk.alliance.services.coursework.FeedbackCourseworkService;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;
import ru.kozelsk.alliance.utils.services.CheckAuthorizeService;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/coursework")
public class CourseworkMainController {

    private final CheckAuthorizeService checkAuthorizeService;
    private final CourseworkService courseworkService;
    private final MyUserDetailsService myUserDetailsService;
    private final FeedbackCourseworkService feedbackCourseworkService;

    @Autowired
    public CourseworkMainController(CheckAuthorizeService checkAuthorizeService, CourseworkService courseworkService, MyUserDetailsService myUserDetailsService, FeedbackCourseworkService feedbackCourseworkService) {
        this.checkAuthorizeService = checkAuthorizeService;
        this.courseworkService = courseworkService;
        this.myUserDetailsService = myUserDetailsService;
        this.feedbackCourseworkService = feedbackCourseworkService;
    }

    @GetMapping
    public String allTour(Model model, @AuthenticationPrincipal OAuth2User oAuth2User, Principal principal) {

        model.addAttribute("allCoursework", courseworkService.findAll());
        model.addAttribute("feedbackCoursework", feedbackCourseworkService.findAll().stream().filter(FeedbackCoursework::isActive));

        if(oAuth2User != null) {
            Optional<User> user = myUserDetailsService.findByEmail(oAuth2User.getAttribute("email"));
            model.addAttribute("hasFeedback", user.get().isCourseworkFeedback());
        } else if (principal != null) {
            Optional<User> user = myUserDetailsService.findByEmail(principal.getName());
            model.addAttribute("hasFeedback", user.get().isCourseworkFeedback());
        }

        model.addAttribute("newFeedbackCoursework", new FeedbackCoursework());
        model.addAttribute("checkAdmin", checkAuthorizeService.checkAdmin(principal));

        return "coursework/main";
    }
}
