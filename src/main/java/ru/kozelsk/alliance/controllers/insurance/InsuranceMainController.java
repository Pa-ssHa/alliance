package ru.kozelsk.alliance.controllers.insurance;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kozelsk.alliance.models.insurance.FeedbackInsurance;
import ru.kozelsk.alliance.models.realty.FeedbackRealty;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.insurance.FeedbackInsuranceService;
import ru.kozelsk.alliance.services.realty.FeedbackRealtyService;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;
import ru.kozelsk.alliance.utils.services.CheckAuthorizeService;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/insurance")
public class InsuranceMainController {

    private final CheckAuthorizeService checkAuthorizeService;
    private final MyUserDetailsService myUserDetailsService;
    private final FeedbackInsuranceService feedbackInsuranceService;

    @Autowired
    public InsuranceMainController(CheckAuthorizeService checkAuthorizeService, MyUserDetailsService myUserDetailsService, FeedbackInsuranceService feedbackInsuranceService) {
        this.checkAuthorizeService = checkAuthorizeService;
        this.myUserDetailsService = myUserDetailsService;
        this.feedbackInsuranceService = feedbackInsuranceService;
    }

    @GetMapping()
    public String allInsurance(Model model, @AuthenticationPrincipal OAuth2User oAuth2User, Principal principal) {

        model.addAttribute("feedbackInsurance", feedbackInsuranceService.findAll().stream().filter(FeedbackInsurance::isActive));

        if(oAuth2User != null) {
            Optional<User> user = myUserDetailsService.findByEmail(oAuth2User.getAttribute("email"));
            model.addAttribute("hasFeedback", user.get().isInsuranceFeedback());
        } else if (principal != null) {
            Optional<User> user = myUserDetailsService.findByEmail(principal.getName());
            model.addAttribute("hasFeedback", user.get().isInsuranceFeedback());
        }

        model.addAttribute("checkAdmin", checkAuthorizeService.checkAdmin(principal));
        model.addAttribute("newFeedbackInsurance", new FeedbackRealty());

        return "insurance/main";
    }


}
