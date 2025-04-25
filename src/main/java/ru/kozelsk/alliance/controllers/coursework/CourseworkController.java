package ru.kozelsk.alliance.controllers.coursework;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kozelsk.alliance.models.coursework.Coursework;
import ru.kozelsk.alliance.services.coursework.BookingCourseworkService;
import ru.kozelsk.alliance.services.coursework.CourseworkService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;
import ru.kozelsk.alliance.utils.dto.cousework.CourseworkDTO;
import ru.kozelsk.alliance.utils.services.CheckAuthorizeService;
import ru.kozelsk.alliance.utils.services.UserFromPrincipal;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/coursework")
public class CourseworkController {

    private final CourseworkService courseworkService;
    private final UserFromPrincipal userFromPrincipal;
    private final CheckAuthorizeService checkAuthorizeService;
    private final BookingCourseworkService bookingCourseworkService;

    @Autowired
    public CourseworkController(CourseworkService courseworkService, UserFromPrincipal userFromPrincipal, CheckAuthorizeService checkAuthorizeService, BookingCourseworkService bookingCourseworkService) {
        this.courseworkService = courseworkService;
        this.userFromPrincipal = userFromPrincipal;
        this.checkAuthorizeService = checkAuthorizeService;
        this.bookingCourseworkService = bookingCourseworkService;
    }

    @GetMapping("/{id}")
    public String oneCoursework(@PathVariable("id") int id, Model model, Principal principal) {

        Optional<Coursework> coursework = courseworkService.findOne(id);
        model.addAttribute("coursework", coursework.get());


        if (principal != null) {
            model.addAttribute("booking", bookingCourseworkService.getBookingForUser(userFromPrincipal.getUserId(principal)));
        } else {
            model.addAttribute("booking", Optional.empty());
        }

        model.addAttribute("checkAdmin", checkAuthorizeService.checkAdmin(principal));
        model.addAttribute("authenticated", principal!=null);

        return "coursework/coursework/show";
    }

    @IsAdmin
    @GetMapping("/new")
    public String newCoursework(Model model) {
        model.addAttribute("newCoursework", new Coursework());
        return "coursework/coursework/new";
    }

    @IsAdmin
    @PostMapping
    public String createCoursework(@ModelAttribute("newCoursework") CourseworkDTO courseworkDTO,
                                   @RequestParam("image") MultipartFile image, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return "coursework/coursework/new";
        }
        Coursework coursework = new Coursework();

        coursework.setDescription(courseworkDTO.getDescription());
        coursework.setTitle(courseworkDTO.getTitle());
        coursework.setPrice(courseworkDTO.getPrice());

        courseworkService.save(coursework, image);
        return "redirect:/coursework";
    }


    @IsAdmin
    @GetMapping("/edit/{id}")
    public String editCoursework(@PathVariable("id") int id, Model model) {
        Optional<Coursework> coursework = courseworkService.findOne(id);
        model.addAttribute("upCoursework", coursework.get());
        return "coursework/coursework/edit";
    }

    @IsAdmin
    @PostMapping("/edit/{id}")
    public String updateCoursework(@PathVariable("id") int id,
                                   @ModelAttribute("upCoursework") Coursework coursework,
                                   @RequestParam(value = "newImage", required = false) MultipartFile image, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "coursework/coursework/edit";
        }

        if (!image.isEmpty()) {
            courseworkService.updateWithImage(coursework, id, image);
        } else {
            courseworkService.updateWithoutImage(coursework, id);
        }

        return "redirect:/coursework/" + id;
    }


    @IsAdmin
    @PostMapping("/delete/{id}")
    public String deleteCoursework(@PathVariable("id") int id) {
        courseworkService.delete(id);
        return "redirect:/coursework";
    }
}
