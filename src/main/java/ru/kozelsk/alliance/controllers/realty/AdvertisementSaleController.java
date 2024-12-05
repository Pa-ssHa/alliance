package ru.kozelsk.alliance.controllers.realty;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.realty.AdvertisementSale;
import ru.kozelsk.alliance.services.realty.AdvertisementSaleService;

@Controller
@RequestMapping("/realty/advertisementSale")
public class AdvertisementSaleController {

    private final AdvertisementSaleService advertisementSaleService;

    @Autowired
    public AdvertisementSaleController(AdvertisementSaleService advertisementSaleService) {
        this.advertisementSaleService = advertisementSaleService;
    }

    @GetMapping("/{id}")
    public String showAdvertisementSale(@PathVariable("id") int id, Model model) {

        model.addAttribute("oneAdvertisementSale", advertisementSaleService.findOne(id));
        return "realty/advertisementSale/show";
    }


    @GetMapping("/new")
    public String newAdvertisementSale(@ModelAttribute("newAdvertisementSale") AdvertisementSale advertisementSale) {
        return "realty/advertisementSale/new";
    }

    @PostMapping()
    public String createAdvertisementSale(@ModelAttribute("newAdvertisementSale") AdvertisementSale advertisementSale,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "realty/advertisementSale/new";
        advertisementSaleService.save(advertisementSale);
        return "redirect:/realty";
    }

    @GetMapping("/{id}/edit")
    public String editAdvertisementSale(@PathVariable("id") int id, Model model) {
        model.addAttribute("upAdvertisementSale", advertisementSaleService.findOne(id));

        return "realty/advertisementSale/edit";
    }

    @PatchMapping("/{id}")
    public String updateAdvertisementSale(@PathVariable("id") int id, @ModelAttribute("upAdvertisementSale") AdvertisementSale upAdvertisementSale,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/realty/advertisementSale/edit";
        advertisementSaleService.update(id, upAdvertisementSale);
        return "redirect:/realty/advertisementSale/"+id;
    }

    @DeleteMapping("/{id}")
    public String deleteAdvertisementSale(@PathVariable("id") int id) {
        advertisementSaleService.delete(id);
        return "redirect:/realty";
    }

}
