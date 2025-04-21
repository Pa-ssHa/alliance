package ru.kozelsk.alliance.controllers.excursion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kozelsk.alliance.models.excursion.Tour;
import ru.kozelsk.alliance.models.excursion.TourForm;
import ru.kozelsk.alliance.models.excursion.TourImage;
import ru.kozelsk.alliance.models.excursion.booking.Booking;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.services.excursion.TourImageService;
import ru.kozelsk.alliance.services.excursion.TourService;
import ru.kozelsk.alliance.services.excursion.booking.BookingService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;
import ru.kozelsk.alliance.utils.services.CheckAuthorizeService;
import ru.kozelsk.alliance.utils.services.UserFromPrincipal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/excursion/tour")
public class TourController {

    private final TourService tourService;
    private final TourImageService tourImageService;
    private final BookingService bookingService;
    private final CheckAuthorizeService checkAuthorizeService;
    private final UserFromPrincipal userFromPrincipal;

    @Autowired
    public TourController(TourService tourService, TourImageService tourImageService, BookingService bookingService, CheckAuthorizeService checkAuthorizeService, UserFromPrincipal userFromPrincipal) {
        this.tourService = tourService;
        this.tourImageService = tourImageService;
        this.bookingService = bookingService;
        this.checkAuthorizeService = checkAuthorizeService;
        this.userFromPrincipal = userFromPrincipal;
    }

    @GetMapping("/{id}")
    public String showTour(@PathVariable("id") int id, Model model, @AuthenticationPrincipal User user, Principal principal) {
        Tour tour = tourService.findOne(id);
        tour.getImages().sort((img1, img2) -> Boolean.compare(img2.isMain(), img1.isMain()));
        model.addAttribute("oneTour", tourService.findOne(id));


        if(principal != null) {
            List<Booking> bookings = bookingService.getBookingForTour(id);
            Optional<Booking> booking = bookingService.isBookingForUser(bookings, userFromPrincipal.getUserId(principal))
                    .filter(b -> b.getBookingTime().isAfter(LocalDateTime.now()));
            model.addAttribute("bookings", booking);
        }else {
            model.addAttribute("bookings", Optional.empty());
        }

        model.addAttribute("checkAdmin", checkAuthorizeService.checkAdmin(principal));

        model.addAttribute("authenticated", principal!=null);

//        List<Booking> bookings = bookingService.getBookingForTour(id);
//        model.addAttribute("bookings", bookings);

        return "excursion/tour/show";
    }


    @IsAdmin
    @GetMapping("/new")
    public String newTour(Model model) {
        model.addAttribute("newTour", new Tour());
        return "excursion/tour/new";
    }

    @IsAdmin
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




    //    Редактирование туров
    @IsAdmin
    @GetMapping("/edit/{id}")
    public String editTour(@PathVariable("id") int id, Model model) {
        Tour tour = tourService.findOne(id);
        tour.getImages().sort((img1, img2) -> Boolean.compare(img2.isMain(), img1.isMain()));

        model.addAttribute("upTour", tourService.findOne(id));
        return "excursion/tour/edit";
    }

    @IsAdmin
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
    @IsAdmin
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
    @IsAdmin
    @PostMapping("setMainImage/{imageId}")
    public String setMainImage(@PathVariable("imageId") int id,
                               @RequestParam("tourId") int tourId) throws IOException {
        tourImageService.setMainImage(id, tourId);
        return "redirect:/excursion/tour/edit/" + tourId;
    }




    ///// удаление тура
    @IsAdmin
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
    @IsAdmin
    @DeleteMapping("/deleteImage/{id}")
    public String deleteImage(@PathVariable("id") int id) throws IOException {
        int temp_id = tourImageService.findOne(id).getTour().getId();
        tourImageService.deleteImage(id);
        return "redirect:/excursion/tour/edit/" + temp_id;
    }

}
