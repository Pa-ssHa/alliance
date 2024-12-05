package ru.kozelsk.alliance.controllers.realty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.realty.AdvertisementRent;
import ru.kozelsk.alliance.models.realty.AdvertisementSale;
import ru.kozelsk.alliance.services.realty.AdvertisementRentService;

@Controller
@RequestMapping("/realty/advertisementRent")
public class AdvertisementRentController {

    private final AdvertisementRentService advertisementRentService;

    @Autowired
    public AdvertisementRentController(AdvertisementRentService advertisementRentService) {
        this.advertisementRentService = advertisementRentService;
    }

    @GetMapping("/{id}")
    public String showAdvertisementSale(@PathVariable("id") int id, Model model) {

        model.addAttribute("oneAdvertisementRent", advertisementRentService.findOne(id));
        return "realty/advertisementRent/show";
    }


    @GetMapping("/new")
    public String newAdvertisementSale(@ModelAttribute("newAdvertisementRent") AdvertisementRent advertisementRent) {
        return "realty/advertisementRent/new";
    }

    @PostMapping()
    public String createAdvertisementSale(@ModelAttribute("newAdvertisementRent") AdvertisementRent advertisementRent,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "realty/advertisementRent/new";
        advertisementRentService.save(advertisementRent);
        return "redirect:/realty";
    }

    @GetMapping("/{id}/edit")
    public String editAdvertisementRent(@PathVariable("id") int id, Model model) {
        model.addAttribute("upAdvertisementRent", advertisementRentService.findOne(id));

        return "realty/advertisementRent/edit";
    }

    @PatchMapping("/{id}")
    public String updateAdvertisementRent(@PathVariable("id") int id, @ModelAttribute("upAdvertisementRent") AdvertisementRent upAdvertisementRent,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/realty/advertisementRent/edit";
        advertisementRentService.update(id, upAdvertisementRent);
        return "redirect:/realty/advertisementRent/"+id;

    }

    @DeleteMapping("/{id}")
    public String deleteAdvertisementRent(@PathVariable("id") int id) {
        advertisementRentService.delete(id);
        return "redirect:/realty";
    }
}
