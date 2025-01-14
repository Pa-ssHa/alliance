package ru.kozelsk.alliance.controllers.excursion;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kozelsk.alliance.services.excursion.TourImageService;
import ru.kozelsk.alliance.services.excursion.TourService;

@Controller
@RequestMapping("/excursion")
public class ExcursionMainController {

    private final TourService tourService;

    @Autowired
    public ExcursionMainController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping()
    public String allTour(Model model){
        model.addAttribute("allTour", tourService.findAll());
        return "excursion/main";
    }
}
