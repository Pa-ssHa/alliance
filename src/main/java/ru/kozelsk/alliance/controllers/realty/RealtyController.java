package ru.kozelsk.alliance.controllers.realty;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.realty.AdvertisementSale;
import ru.kozelsk.alliance.services.realty.AdvertisementSaleService;
import ru.kozelsk.alliance.services.realty.FeedbackService;

@Controller
@RequestMapping("/realty")
public class RealtyController {

    private final AdvertisementSaleService advertisementSaleService;
    private final FeedbackService feedbackService;

    @Autowired
    public RealtyController(AdvertisementSaleService advertisementSaleService, FeedbackService feedbackService) {
        this.advertisementSaleService = advertisementSaleService;
        this.feedbackService = feedbackService;
    }

    @GetMapping()
    public String allAdvertisementSale(Model model) {
        model.addAttribute("advertisementsSale", advertisementSaleService.findAll());
        model.addAttribute("feedbacksSale", feedbackService.findAll());
        return "realty/main";
    }

    @GetMapping("/advertisementSale/{id}")
    public String showAdvertisementSale(@PathVariable("id") int id, Model model) {

        model.addAttribute("oneAdvertisementSale", advertisementSaleService.findOne(id));


        return "realty/advertisementSale/show";
    }


    @GetMapping("/advertisementSale/new")
    public String newAdvertisementSale(@ModelAttribute("newAdvertisementSale") AdvertisementSale advertisementSale) {
        return "realty/advertisementSale/new";
    }

    @PostMapping("/advertisementSale")
    public String createAdvertisementSale(@ModelAttribute("newAdvertisementSale") AdvertisementSale advertisementSale,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "realty/advertisementSale/new";
        advertisementSaleService.save(advertisementSale);
        return "redirect:/realty";
    }

    @GetMapping("/advertisementSale/{id}/edit")
    public String editAdvertisementSale(@PathVariable("id") int id, Model model) {
        model.addAttribute("advertisementSale", advertisementSaleService.findOne(id));

        return "realty/advertisementSale/edit";
    }

    @PatchMapping("/advertisementSale/{id}")
    public String updateAdvertisementSale(@PathVariable("id") int id, BindingResult bindingResult,
                                          @ModelAttribute("upAdvertisementSale") AdvertisementSale upAdvertisementSale) {
        if (bindingResult.hasErrors())
            return "/realty/advertisementSale/edit";
        advertisementSaleService.update(id, upAdvertisementSale);
        return "redirect:/realty/advertisementSale/"+id;

    }

    @DeleteMapping("/advertisementSale/{id}")
    public String deleteAdvertisementSale(@PathVariable("id") int id) {
        advertisementSaleService.delete(id);
        return "redirect:/realty";
    }


}
