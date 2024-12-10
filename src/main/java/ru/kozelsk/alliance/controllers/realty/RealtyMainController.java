package ru.kozelsk.alliance.controllers.realty;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.services.realty.AdvertisementRentService;
import ru.kozelsk.alliance.services.realty.AdvertisementSaleService;
import ru.kozelsk.alliance.services.realty.FeedbackRealtyService;

@Controller
@RequestMapping("/realty")
public class RealtyMainController {

    private final AdvertisementSaleService advertisementSaleService;
    private final AdvertisementRentService advertisementRentService;
    private final FeedbackRealtyService feedbackRealtyService;

    @Autowired
    public RealtyMainController(AdvertisementSaleService advertisementSaleService, FeedbackRealtyService feedbackRealtyService, AdvertisementRentService advertisementRentService) {
        this.advertisementSaleService = advertisementSaleService;
        this.feedbackRealtyService = feedbackRealtyService;
        this.advertisementRentService = advertisementRentService;
    }

    @GetMapping()
    public String allAdvertisementSale(Model model) {
        model.addAttribute("advertisementsSale", advertisementSaleService.findAll());
        model.addAttribute("advertisementsRent", advertisementRentService.findAll());
        model.addAttribute("feedbacksRealty", feedbackRealtyService.findAll());
        return "realty/main";
    }

}
