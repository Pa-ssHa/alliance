package ru.kozelsk.alliance.controllers.realty;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.realty.FeedbackRealty;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.realty.AdvertisementRentService;
import ru.kozelsk.alliance.services.realty.AdvertisementSaleService;
import ru.kozelsk.alliance.services.realty.FeedbackRealtyService;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;
import ru.kozelsk.alliance.utils.services.CheckAuthorizeService;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/realty")
public class RealtyMainController {

    private final AdvertisementSaleService advertisementSaleService;
    private final AdvertisementRentService advertisementRentService;
    private final FeedbackRealtyService feedbackRealtyService;
    private final MyUserDetailsService myUserDetailsService;
    private final CheckAuthorizeService checkAuthorizeService;

    @Autowired
    public RealtyMainController(AdvertisementSaleService advertisementSaleService, FeedbackRealtyService feedbackRealtyService, AdvertisementRentService advertisementRentService, MyUserDetailsService myUserDetailsService, CheckAuthorizeService checkAuthorizeService) {
        this.advertisementSaleService = advertisementSaleService;
        this.feedbackRealtyService = feedbackRealtyService;
        this.advertisementRentService = advertisementRentService;
        this.myUserDetailsService = myUserDetailsService;
        this.checkAuthorizeService = checkAuthorizeService;
    }

    @GetMapping()
    public String allAdvertisementSale(Model model, @AuthenticationPrincipal OAuth2User oAuth2User, Principal principal) {


        model.addAttribute("advertisementsSale", advertisementSaleService.findAll());
        model.addAttribute("advertisementsRent", advertisementRentService.findAll());
        model.addAttribute("feedbacksRealty", feedbackRealtyService.findAll());



        if(oAuth2User != null) {
            Optional<User> user = myUserDetailsService.findByEmail(oAuth2User.getAttribute("email"));
            model.addAttribute("hasFeedback", user.get().isRealtyFeedback());
        } else if (principal != null) {
            Optional<User> user = myUserDetailsService.findByEmail(principal.getName());
            model.addAttribute("hasFeedback", user.get().isRealtyFeedback());
        }

        model.addAttribute("checkAdmin", checkAuthorizeService.checkAdmin(principal));
        model.addAttribute("newFeedbackRealty", new FeedbackRealty());

        return "realty/main";
    }

}
