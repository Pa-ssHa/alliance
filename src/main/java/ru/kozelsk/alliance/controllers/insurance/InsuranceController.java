package ru.kozelsk.alliance.controllers.insurance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kozelsk.alliance.models.coursework.Coursework;
import ru.kozelsk.alliance.models.insurance.Insurance;
import ru.kozelsk.alliance.services.insurance.InsuranceService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;
import ru.kozelsk.alliance.utils.dto.cousework.CourseworkDTO;
import ru.kozelsk.alliance.utils.dto.insurance.InsuranceDTO;
import ru.kozelsk.alliance.utils.services.CheckAuthorizeService;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/insurance")
public class InsuranceController {

    private final InsuranceService insuranceService;
    private final CheckAuthorizeService checkAuthorizeService;

    @Autowired
    public InsuranceController(InsuranceService insuranceService, CheckAuthorizeService checkAuthorizeService) {
        this.insuranceService = insuranceService;
        this.checkAuthorizeService = checkAuthorizeService;
    }

    @GetMapping("/{id}")
    public String oneInsurance(@PathVariable("id") int id, Model model, Principal principal) {

        Optional<Insurance> insurance = insuranceService.findOne(id);
        model.addAttribute("insurance", insurance.get());

        model.addAttribute("checkAdmin", checkAuthorizeService.checkAdmin(principal));
        model.addAttribute("authenticated", principal!=null);

        return "insurance/insurance/show";
    }

    @IsAdmin
    @GetMapping("/new")
    public String newInsurance(Model model) {
        model.addAttribute("newInsurance", new Insurance());
        return "insurance/insurance/new";
    }

    @IsAdmin
    @PostMapping
    public String createInsurance(@ModelAttribute("newInsurance") InsuranceDTO insuranceDTO,
                                   @RequestParam("image") MultipartFile image, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return "insurance/insurance/new";
        }
        Insurance insurance = new Insurance();

        insurance.setDescription(insuranceDTO.getDescription());
        insurance.setTitle(insuranceDTO.getTitle());
        insurance.setPrice(insuranceDTO.getPrice());

        insuranceService.save(insurance, image);
        return "redirect:/insurance";
    }


    @IsAdmin
    @GetMapping("/edit/{id}")
    public String editCoursework(@PathVariable("id") int id, Model model) {
        Optional<Insurance> insurance = insuranceService.findOne(id);
        model.addAttribute("upInsurance", insurance.get());
        return "insurance/insurance/edit";
    }

    @IsAdmin
    @PostMapping("/edit/{id}")
    public String updateCoursework(@PathVariable("id") int id,
                                   @ModelAttribute("upInsurance") Insurance insurance,
                                   @RequestParam(value = "newImage", required = false) MultipartFile image, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "insurance/insurance/edit";
        }

        if (!image.isEmpty()) {
            insuranceService.updateWithImage(insurance, id, image);
        } else {
            insuranceService.updateWithoutImage(insurance, id);
        }

        return "redirect:/insurance/" + id;
    }


    @IsAdmin
    @PostMapping("/delete/{id}")
    public String deleteCoursework(@PathVariable("id") int id) {
        insuranceService.delete(id);
        return "redirect:/insurance";
    }
}
