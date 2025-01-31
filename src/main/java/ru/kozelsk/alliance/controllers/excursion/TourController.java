package ru.kozelsk.alliance.controllers.excursion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kozelsk.alliance.models.excursion.Tour;
import ru.kozelsk.alliance.models.excursion.TourForm;
import ru.kozelsk.alliance.models.excursion.TourImage;
import ru.kozelsk.alliance.models.excursion.booking.Booking;
import ru.kozelsk.alliance.services.excursion.TourImageService;
import ru.kozelsk.alliance.services.excursion.TourService;
import ru.kozelsk.alliance.services.excursion.booking.BookingService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/excursion/tour")
public class TourController {

    private final TourService tourService;
    private final TourImageService tourImageService;
    private final BookingService bookingService;

    @Autowired
    public TourController(TourService tourService, TourImageService tourImageService, BookingService bookingService) {
        this.tourService = tourService;
        this.tourImageService = tourImageService;
        this.bookingService = bookingService;
    }

    @GetMapping("/{id}")
    public String showTour(@PathVariable("id") int id, Model model) {
        Tour tour = tourService.findOne(id);
        tour.getImages().sort((img1, img2) -> Boolean.compare(img2.isMain(), img1.isMain()));
        model.addAttribute("oneTour", tourService.findOne(id));

        List<Booking> bookings = bookingService.getBookingForTour(id);
        model.addAttribute("bookings", bookings);

        return "excursion/tour/show";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String newTour(Model model) {
        model.addAttribute("newTour", new Tour());
        return "excursion/tour/new";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public String createTour(@ModelAttribute("newTour") TourForm tourForm,
                             @RequestParam("images")MultipartFile[] imageFiles,
                             @RequestParam(value = "mainImage", required = false) Integer mainImageIndex,
                             BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "excursion/tour/new";
        }

        Tour tour = new Tour();
        tour.setTitle(tourForm.getTitle());
        tour.setDescription(tourForm.getDescription());
        tour.setPrice(tourForm.getPrice());

        List<TourImage> images = new ArrayList<>();
        for(int i = 0; i < imageFiles.length; i++) {
            MultipartFile imageFile = imageFiles[i];
            if (!imageFile.isEmpty()) {
                String filename = imageFile.getOriginalFilename();
                String imagePath = TourImageService.saveImageWithName(imageFile);
                TourImage image = new TourImage();
                image.setFilename(filename);
                image.setImagePath(imagePath);
                image.setTour(tour);

                if(mainImageIndex == null && i ==0){
                    image.setMain(true);
                }else {
                    image.setMain(i == mainImageIndex);
                }
                images.add(image);
            }
        }
        tour.setImages(images);

        tourService.save(tour);
        return "redirect:/excursion";
    }




    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editTour(@PathVariable("id") int id, Model model) {
        Tour tour = tourService.findOne(id);
        tour.getImages().sort((img1, img2) -> Boolean.compare(img2.isMain(), img1.isMain()));

        model.addAttribute("upTour", tourService.findOne(id));
        return "excursion/tour/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public String updateTour(@PathVariable("id") int id,
                             @ModelAttribute("upTour") Tour upTour,
                             @RequestParam(value = "newImages", required = true) MultipartFile[] newImages,
                             BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "excursion/tour/edit";
        }

        tourService.update(id, upTour);

        if(newImages != null) {
            for(MultipartFile imageFile : newImages){
                if (!imageFile.isEmpty()) {
                    String imagePath = TourImageService.saveImageWithName(imageFile);
                    TourImage image = new TourImage();
                    image.setFilename(imageFile.getOriginalFilename());
                    image.setImagePath(imagePath);
                    image.setTour(upTour);
                    tourImageService.save(image);
                }
            }
        }
        return "redirect:/excursion/tour/edit/" + id;
    }

    //// замена фото
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/updateImage/{id}")
    public String updateImage(@PathVariable("id") int id,
                              @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        TourImage oldImage = tourImageService.findOne(id);
        if (!imageFile.isEmpty()) {

            Path oldImagePath = Paths.get(oldImage.getImagePath());
            Files.deleteIfExists(oldImagePath);

            String newImagePath = TourImageService.saveImageWithName(imageFile);
            oldImage.setImagePath(newImagePath);
            oldImage.setFilename(imageFile.getOriginalFilename());

            tourImageService.save(oldImage);
        }
        return "redirect:/excursion/tour/edit/" + oldImage.getTour().getId();
    }

    /// сделать фото главным
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("setMainImage/{imageId}")
    public String setMainImage(@PathVariable("imageId") int id,
                               @RequestParam("tourId") int tourId) throws IOException {
        tourImageService.setMainImage(id, tourId);
        return "redirect:/excursion/tour/edit/" + tourId;
    }




    ///// удаление тура
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteTour(@PathVariable("id") int id) throws IOException {
        Tour tour = tourService.findOne(id);
        for (TourImage image : tour.getImages()) {
            tourImageService.deleteImage(image.getId());
        }

        tourService.delete(id);
        return "redirect:/excursion";
    }

    //// удаление фото
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteImage/{id}")
    public String deleteImage(@PathVariable("id") int id) throws IOException {
        int temp_id = tourImageService.findOne(id).getTour().getId();
        tourImageService.deleteImage(id);
        return "redirect:/excursion/tour/edit/" + temp_id;
    }

}
