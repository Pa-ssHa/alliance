package ru.kozelsk.alliance.controllers.excursion;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kozelsk.alliance.services.excursion.TourImageService;
import ru.kozelsk.alliance.services.excursion.TourService;
import ru.kozelsk.alliance.utils.services.CheckAuthorizeService;

import java.security.Principal;

@Controller
@RequestMapping("/excursion")
public class ExcursionMainController {

    private final TourService tourService;
    private final CheckAuthorizeService checkAuthorizeService;

    @Autowired
    public ExcursionMainController(TourService tourService, CheckAuthorizeService checkAuthorizeService) {
        this.tourService = tourService;
        this.checkAuthorizeService = checkAuthorizeService;
    }

    @GetMapping()
    public String allTour(Model model, Principal principal) {
        model.addAttribute("allTour", tourService.findAll());
        model.addAttribute("checkAdmin", checkAuthorizeService.checkAdmin(principal));
        return "excursion/main";
    }
}
