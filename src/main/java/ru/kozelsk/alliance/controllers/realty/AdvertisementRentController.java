package ru.kozelsk.alliance.controllers.realty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kozelsk.alliance.models.realty.*;
import ru.kozelsk.alliance.services.realty.AdvertisementRentImageService;
import ru.kozelsk.alliance.services.realty.AdvertisementRentService;
import ru.kozelsk.alliance.services.realty.AdvertisementSaleImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/realty/advertisementRent")
public class AdvertisementRentController {

    private final AdvertisementRentService advertisementRentService;
    private final AdvertisementRentImageService advertisementRentImageService;

    @Autowired
    public AdvertisementRentController(AdvertisementRentService advertisementRentService, AdvertisementRentImageService advertisementRentImageService) {
        this.advertisementRentService = advertisementRentService;
        this.advertisementRentImageService = advertisementRentImageService;
    }

    @GetMapping("/{id}")
    public String showAdvertisementRent(@PathVariable("id") int id, Model model) {
        AdvertisementRent advertisementRent = advertisementRentService.findOne(id);
        advertisementRent.getImages().sort((img1, img2) -> Boolean.compare(img2.isMain(), img1.isMain()));

        model.addAttribute("oneAdvertisementRent", advertisementRentService.findOne(id));
        return "realty/advertisementRent/show";
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
//    @PreAuthorize("isAuthenticated()")
    public String newAdvertisementRent(Model model) {
        model.addAttribute("newAdvertisementRent", new AdvertisementRentForm());
        return "realty/advertisementRent/new";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public String createAdvertisementRent(@ModelAttribute("newAdvertisementRent") AdvertisementRentForm advertisementRentForm,
                                          @RequestParam("images") MultipartFile[] imageFiles,
                                          @RequestParam("mainImage") int mainImageIndex,
                                          BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors())
            return "realty/advertisementSale/new";

        AdvertisementRent advertisementRent = new AdvertisementRent();
        advertisementRent.setTitle(advertisementRentForm.getTitle());
        advertisementRent.setDescription(advertisementRentForm.getDescription());
        advertisementRent.setPrice(advertisementRentForm.getPrice());

        List<AdvertisementRentImage> images = new ArrayList<>();
        for (int i = 0; i < imageFiles.length; i++) {
            MultipartFile imageFile = imageFiles[i];
            if (!imageFile.isEmpty()) {
                String fileName = imageFile.getOriginalFilename();
                String imagePath = AdvertisementRentImageService.saveImageWithName(imageFile);
                AdvertisementRentImage image = new AdvertisementRentImage();
                image.setFilename(fileName);
                image.setImagePath(imagePath);
                image.setAdvertisement(advertisementRent);
                image.setMain(i == mainImageIndex);
                images.add(image);
            }
        }
        advertisementRent.setImages(images);

        advertisementRentService.save(advertisementRent);
        return "redirect:/realty";
    }




    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
//    @PreAuthorize("isAuthenticated()")
    public String editAdvertisementSale(@PathVariable("id") int id, Model model) {
        AdvertisementRent advertisementRent = advertisementRentService.findOne(id);
        advertisementRent.getImages().sort((img1, img2) -> Boolean.compare(img2.isMain(), img1.isMain()));

        model.addAttribute("upAdvertisementRent", advertisementRentService.findOne(id));

        return "realty/advertisementRent/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public String updateAdvertisementSale(@PathVariable("id") int id, @ModelAttribute("upAdvertisementRent") AdvertisementRent upAdvertisementRent,
                                          @RequestParam(value = "newImages", required = true) MultipartFile[] newImages,
                                          BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors())
            return "/realty/advertisementRent/edit";

        advertisementRentService.update(id, upAdvertisementRent);

        if (newImages != null) {
            for (MultipartFile imageFile : newImages) {
                if (!imageFile.isEmpty()) {
                    String imagePath = AdvertisementRentImageService.saveImageWithName(imageFile);
                    AdvertisementRentImage image = new AdvertisementRentImage();
                    image.setFilename(imageFile.getOriginalFilename());
                    image.setImagePath(imagePath);
                    image.setAdvertisement(upAdvertisementRent);
                    advertisementRentImageService.save(image);
                }
            }
        }

        return "redirect:/realty/advertisementRent/edit/" + id;
    }

    ////////////////////// замена фото
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/updateImage/{id}")
    public String updateImage(@PathVariable("id") int id,
                              @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        AdvertisementRentImage oldImage = advertisementRentImageService.findOne(id);
        if (!imageFile.isEmpty()) {

            Path oldImagePath = Paths.get(oldImage.getImagePath());
            Files.deleteIfExists(oldImagePath);

            String newImagePath = AdvertisementRentImageService.saveImageWithName(imageFile);
            oldImage.setImagePath(newImagePath);
            oldImage.setFilename(imageFile.getOriginalFilename());

            advertisementRentImageService.save(oldImage);
        }
        return "redirect:/realty/advertisementRent/edit/" + oldImage.getAdvertisement().getId();
    }
//  Сделать фото главным
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("setMainImage/{imageId}")
    public String setMainImage(@PathVariable("imageId") int imageId, @RequestParam("advertisementId") int advertisementId) throws IOException {
        advertisementRentImageService.setMainImage(imageId, advertisementId);
        return "redirect:/realty/advertisementRent/edit/" + advertisementId;
    }



//      Удаление объявления
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("isAuthenticated()")
    public String deleteAdvertisementSale(@PathVariable("id") int id) {
        AdvertisementRent advertisementRent = advertisementRentService.findOne(id);
        for (AdvertisementRentImage image : advertisementRent.getImages()) {
            advertisementRentImageService.deleteImage(image.getId());
        }

        advertisementRentService.delete(id);
        return "redirect:/realty";
    }

//    удаление фото
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteImage/{id}")
    public String deleteImage(@PathVariable("id") int id) {
        int temp_id = advertisementRentImageService.findOne(id).getAdvertisement().getId();
        advertisementRentImageService.deleteImage(id);
        return "redirect:/realty/advertisementRent/edit/" + temp_id;
    }
}
