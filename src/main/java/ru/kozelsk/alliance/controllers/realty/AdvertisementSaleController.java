package ru.kozelsk.alliance.controllers.realty;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kozelsk.alliance.models.realty.AdvertisementSaleForm;
import ru.kozelsk.alliance.models.realty.AdvertisementSaleImage;
import ru.kozelsk.alliance.models.realty.AdvertisementSale;
import ru.kozelsk.alliance.services.realty.AdvertisementSaleImageService;
import ru.kozelsk.alliance.services.realty.AdvertisementSaleService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/realty/advertisementSale")
public class AdvertisementSaleController {

    private final AdvertisementSaleService advertisementSaleService;
    private final AdvertisementSaleImageService advertisementSaleImageService;

    @Autowired
    public AdvertisementSaleController(AdvertisementSaleService advertisementSaleService, AdvertisementSaleImageService advertisementSaleImageService) {
        this.advertisementSaleService = advertisementSaleService;
        this.advertisementSaleImageService = advertisementSaleImageService;
    }

    @GetMapping("/{id}")
    public String showAdvertisementSale(@PathVariable("id") int id, Model model) {

        model.addAttribute("oneAdvertisementSale", advertisementSaleService.findOne(id));
        return "realty/advertisementSale/show";
    }


    /*@GetMapping("/new")
    public String newAdvertisementSale(@ModelAttribute("newAdvertisementSale") AdvertisementSale advertisementSale) {
        return "realty/advertisementSale/new";
    }*/

    /*@PostMapping()
    public String createAdvertisementSale(@ModelAttribute("newAdvertisementSale") AdvertisementSale advertisementSale,
                                          @RequestParam("images") MultipartFile[] imageFiles,
                                          BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors())
            return "realty/advertisementSale/new";

        advertisementSaleService.saveAdvertisementWithImage(advertisementSale, imageFiles);
        return "redirect:/realty";
    }*/


    @GetMapping("/new")
    public String newAdvertisementSale(Model model) {
        model.addAttribute("newAdvertisementSale", new AdvertisementSaleForm());
        return "realty/advertisementSale/new";
    }

    @PostMapping()
    public String createAdvertisementSale(@ModelAttribute("newAdvertisementSale") AdvertisementSaleForm advertisementSaleForm,
                                          @RequestParam("images") MultipartFile[] imageFiles ,
                                          BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors())
            return "realty/advertisementSale/new";

        AdvertisementSale advertisementSale = new AdvertisementSale();
        advertisementSale.setTitle(advertisementSaleForm.getTitle());
        advertisementSale.setDescription(advertisementSaleForm.getDescription());
        advertisementSale.setPrice(advertisementSaleForm.getPrice());

        List<AdvertisementSaleImage> images = new ArrayList<>();
        for (MultipartFile imageFile : advertisementSaleForm.getImages()) {
            if (!imageFile.isEmpty()) {
                String fileName = imageFile.getOriginalFilename();
                String imagePath = AdvertisementSaleImageService.saveImage(imageFile);
                AdvertisementSaleImage image = new AdvertisementSaleImage();
                image.setFilename(fileName);
                image.setImagePath(imagePath);
                image.setAdvertisement(advertisementSale);
                images.add(image);
            }
        }
        advertisementSale.setImages(images);

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
        AdvertisementSale advertisementSale = advertisementSaleService.findOne(id);
        /*for (AdvertisementSaleImage image : advertisementSale.getImages()) {
            advertisementSaleImageService.deleteImage(image.getImagePath());
        }*/

        advertisementSaleService.delete(id);
        return "redirect:/realty";
    }

}
