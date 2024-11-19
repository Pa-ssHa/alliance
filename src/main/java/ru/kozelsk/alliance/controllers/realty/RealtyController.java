package ru.kozelsk.alliance.controllers.realty;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.realty.Advertisement;
import ru.kozelsk.alliance.services.realty.AdvertisementService;
import ru.kozelsk.alliance.services.realty.FeedbackService;

@Controller
@RequestMapping("/realty")
public class RealtyController {

    private final AdvertisementService advertisementService;
    private final FeedbackService feedbackService;

    @Autowired
    public RealtyController(AdvertisementService advertisementService, FeedbackService feedbackService) {
        this.advertisementService = advertisementService;
        this.feedbackService = feedbackService;
    }

    @GetMapping()
    public String allAdvertisement(Model model) {
        model.addAttribute("advertisements", advertisementService.findAll());
        model.addAttribute("feedbacks", feedbackService.findAll());
        return "realty/main";
    }

    @GetMapping("/advertisement/{id}")
    public String showAdvertisement(@PathVariable("id") int id, Model model) {

        model.addAttribute("oneAdvertisement", advertisementService.findOne(id));


        return "realty/advertisement/show";
    }


    @GetMapping("/advertisement/new")
    public String newAdvertisement(@ModelAttribute("newAdvertisement") Advertisement advertisement) {
        return "realty/advertisement/new";
    }

    @PostMapping("/advertisement")
    public String createAdvertisement(@ModelAttribute("newAdvertisement") Advertisement advertisement,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "realty/advertisement/new";
        advertisementService.save(advertisement);
        return "redirect:/realty/main";
    }

    @GetMapping("/advertisement/{id}/edit")
    public String editAdvertisement(@PathVariable("id") int id, Model model) {
        model.addAttribute("advertisement", advertisementService.findOne(id));

        return "realty/advertisement/edit";
    }

    @PatchMapping("/advertisement/{id}")
    public String updateAdvertisement(@PathVariable("id") int id, BindingResult bindingResult,
                                      @ModelAttribute("upAdvertisement") Advertisement upAdvertisement) {
        if (bindingResult.hasErrors())
            return "/realty/advertisement/edit";
        advertisementService.update(id, upAdvertisement);
        return "redirect:/realty/advertisement/"+id;

    }


}
