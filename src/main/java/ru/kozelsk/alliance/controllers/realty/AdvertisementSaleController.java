package ru.kozelsk.alliance.controllers.realty;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kozelsk.alliance.models.realty.AdvertisementSaleForm;
import ru.kozelsk.alliance.models.realty.AdvertisementSaleImage;
import ru.kozelsk.alliance.models.realty.AdvertisementSale;
import ru.kozelsk.alliance.repositories.realty.AdvertisementSaleImageRepository;
import ru.kozelsk.alliance.services.realty.AdvertisementSaleImageService;
import ru.kozelsk.alliance.services.realty.AdvertisementSaleService;

import java.awt.*;
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
    private final AdvertisementSaleImageRepository advertisementSaleImageRepository;

    @Autowired
    public AdvertisementSaleController(AdvertisementSaleService advertisementSaleService, AdvertisementSaleImageService advertisementSaleImageService, AdvertisementSaleImageRepository advertisementSaleImageRepository) {
        this.advertisementSaleService = advertisementSaleService;
        this.advertisementSaleImageService = advertisementSaleImageService;
        this.advertisementSaleImageRepository = advertisementSaleImageRepository;
    }

    @GetMapping("/{id}")
    public String showAdvertisementSale(@PathVariable("id") int id, Model model) {
        AdvertisementSale advertisementSale = advertisementSaleService.findOne(id);
        advertisementSale.getImages().sort((img1, img2) -> Boolean.compare(img2.isMain(), img1.isMain()));

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
                                          @RequestParam("images") MultipartFile[] imageFiles,
                                          @RequestParam("mainImage") int mainImageIndex,
                                          BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors())
            return "realty/advertisementSale/new";

        AdvertisementSale advertisementSale = new AdvertisementSale();
        advertisementSale.setTitle(advertisementSaleForm.getTitle());
        advertisementSale.setDescription(advertisementSaleForm.getDescription());
        advertisementSale.setPrice(advertisementSaleForm.getPrice());

        List<AdvertisementSaleImage> images = new ArrayList<>();
        for (int i = 0; i < imageFiles.length; i++) {
            MultipartFile imageFile = imageFiles[i];
            if (!imageFile.isEmpty()) {
                String fileName = imageFile.getOriginalFilename();
                String imagePath = AdvertisementSaleImageService.saveImageWithName(imageFile);
                AdvertisementSaleImage image = new AdvertisementSaleImage();
                image.setFilename(fileName);
                image.setImagePath(imagePath);
                image.setAdvertisement(advertisementSale);
                image.setMain(i == mainImageIndex);
                images.add(image);
            }
        }
        advertisementSale.setImages(images);

        advertisementSaleService.save(advertisementSale);
        return "redirect:/realty";
    }




    @GetMapping("/edit/{id}")
    public String editAdvertisementSale(@PathVariable("id") int id, Model model) {
        AdvertisementSale advertisementSale = advertisementSaleService.findOne(id);
        advertisementSale.getImages().sort((img1, img2) -> Boolean.compare(img2.isMain(), img1.isMain()));

        model.addAttribute("upAdvertisementSale", advertisementSaleService.findOne(id));

        return "realty/advertisementSale/edit";
    }

    @PatchMapping("/{id}")
    public String updateAdvertisementSale(@PathVariable("id") int id, @ModelAttribute("upAdvertisementSale") AdvertisementSale upAdvertisementSale,
                                          @RequestParam(value = "newImages", required = true) MultipartFile[] newImages,
                                          BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors())
            return "/realty/advertisementSale/edit";

        advertisementSaleService.update(id, upAdvertisementSale);

        if (newImages != null) {
            for (MultipartFile imageFile : newImages) {
                if (!imageFile.isEmpty()) {
                    String imagePath = AdvertisementSaleImageService.saveImageWithName(imageFile);
                    AdvertisementSaleImage image = new AdvertisementSaleImage();
                    image.setFilename(imageFile.getOriginalFilename());
                    image.setImagePath(imagePath);
                    image.setAdvertisement(upAdvertisementSale);
                    advertisementSaleImageService.save(image);
                }
            }
        }

        return "redirect:/realty/advertisementSale/edit/" + id;
    }

    ////////////////////// замена фото
    @PostMapping("/updateImage/{id}")
    public String updateImage(@PathVariable("id") int id,
                              @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        AdvertisementSaleImage oldImage = advertisementSaleImageService.findOne(id);
        if (!imageFile.isEmpty()) {

            Path oldImagePath = Paths.get(oldImage.getImagePath());
            Files.deleteIfExists(oldImagePath);

            String newImagePath = AdvertisementSaleImageService.saveImageWithName(imageFile);
            oldImage.setImagePath(newImagePath);
            oldImage.setFilename(imageFile.getOriginalFilename());

            advertisementSaleImageService.save(oldImage);
        }
        return "redirect:/realty/advertisementSale/edit/" + oldImage.getAdvertisement().getId();
    }

    @PostMapping("setMainImage/{imageId}")
    public String setMainImage(@PathVariable("imageId") int imageId, @RequestParam("advertisementId") int advertisementId) throws IOException {
        advertisementSaleImageService.setMainImage(imageId, advertisementId);
        return "redirect:/realty/advertisementSale/edit/" + advertisementId;
    }





    @DeleteMapping("/{id}")
    public String deleteAdvertisementSale(@PathVariable("id") int id) {
        AdvertisementSale advertisementSale = advertisementSaleService.findOne(id);
        for (AdvertisementSaleImage image : advertisementSale.getImages()) {
            advertisementSaleImageService.deleteImage(image.getId());
        }

        advertisementSaleService.delete(id);
        return "redirect:/realty";
    }

    @DeleteMapping("/deleteImage/{id}")
    public String deleteImage(@PathVariable("id") int id) {
        int temp_id = advertisementSaleImageService.findOne(id).getAdvertisement().getId();
        advertisementSaleImageService.deleteImage(id);
        return "redirect:/realty/advertisementSale/edit/" + temp_id;
    }




}
