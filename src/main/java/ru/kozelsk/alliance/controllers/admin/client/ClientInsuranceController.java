package ru.kozelsk.alliance.controllers.admin.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.insurance.admin.ClientInsurance;
import ru.kozelsk.alliance.services.insurance.admin.ClientInsuranceService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;

@Controller
@RequestMapping("/admin/client-insurance")
public class ClientInsuranceController {

    private final ClientInsuranceService clientInsuranceService;

    @Autowired
    public ClientInsuranceController(ClientInsuranceService clientInsuranceService) {
        this.clientInsuranceService = clientInsuranceService;
    }

    @IsAdmin
    @GetMapping()
    public String allClients(Model model) {
        model.addAttribute("clients", clientInsuranceService.findAll());
        return "admin/client/insurance/main";
    }


    @IsAdmin
    @GetMapping("/new")
    public String newClientInsurance(Model model) {
        model.addAttribute("newClient", new ClientInsurance());
        return "admin/client/insurance/new";
    }

    @IsAdmin
    @PostMapping()
    public String createClientInsurance(@ModelAttribute("newClient") ClientInsurance clientInsurance,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/client/insurance/new";
        }

        if(clientInsurance.getPhone() != null && !clientInsurance.getPhone().startsWith("+7")) {
            clientInsurance.setPhone(clientInsurance.getPhone());
        }

        clientInsuranceService.save(clientInsurance);
        return "redirect:/admin/client-insurance";
    }


    @IsAdmin
    @GetMapping("/edit/{id}")
    public String editClientInsurance(@PathVariable("id") int id, Model model) {
        ClientInsurance clientInsurance = clientInsuranceService.findOne(id).get();
        model.addAttribute("upClient", clientInsurance);
        return "admin/client/insurance/edit";
    }

    @IsAdmin
    @PostMapping("/{id}")
    public String updateClientInsurance(@PathVariable("id") int id,
                                        @ModelAttribute("upClient") ClientInsurance upClientInsurance,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/client/insurance/edit";
        }

        if(upClientInsurance.getPhone() != null && !upClientInsurance.getPhone().startsWith("+7")) {
            upClientInsurance.setPhone(upClientInsurance.getPhone());
        }

        clientInsuranceService.update(upClientInsurance, id);
        return "redirect:/admin/client-insurance";
    }

    @IsAdmin
    @DeleteMapping("/delete/{id}")
    public String deleteClientInsurance(@PathVariable("id") int id) {
        clientInsuranceService.delete(id);
        return "redirect:/admin/client-insurance";
    }
}
