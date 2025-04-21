package ru.kozelsk.alliance.controllers.admin.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.excursion.admin.ClientExcursion;
import ru.kozelsk.alliance.services.excursion.admin.ClientExcursionService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;

@Controller
@RequestMapping("/admin/client-excursion")
public class ClientExcursionController {

    private final ClientExcursionService clientExcursionService;

    @Autowired
    public ClientExcursionController(ClientExcursionService clientExcursionService) {
        this.clientExcursionService = clientExcursionService;
    }

    @IsAdmin
    @GetMapping()
    public String allClients(Model model) {
        model.addAttribute("clients", clientExcursionService.findAll());
        return "admin/client/excursion/main";
    }


    @IsAdmin
    @GetMapping("/new")
    public String newClientExcursion(Model model) {
        model.addAttribute("newClient", new ClientExcursion());
        return "admin/client/excursion/new";
    }

    @IsAdmin
    @PostMapping()
    public String createClientExcursion(@ModelAttribute("newClient") ClientExcursion clientExcursion,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/client/excursion/new";
        }

        if(clientExcursion.getPhone() != null && !clientExcursion.getPhone().startsWith("+7")) {
            clientExcursion.setPhone(clientExcursion.getPhone());
        }

        clientExcursionService.save(clientExcursion);
        return "redirect:/admin/client-excursion";
    }


    @IsAdmin
    @GetMapping("/edit/{id}")
    public String editClientExcursion(@PathVariable("id") int id, Model model) {
        ClientExcursion clientExcursion = clientExcursionService.findOne(id).get();
        model.addAttribute("upClient", clientExcursion);
        return "admin/client/excursion/edit";
    }

    @IsAdmin
    @PostMapping("/{id}")
    public String updateClientExcursion(@PathVariable("id") int id,
                                     @ModelAttribute("upClient") ClientExcursion upClientExcursion,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/client/excursion/edit";
        }

        if(upClientExcursion.getPhone() != null && !upClientExcursion.getPhone().startsWith("+7")) {
            upClientExcursion.setPhone(upClientExcursion.getPhone());
        }

        clientExcursionService.update(upClientExcursion, id);
        return "redirect:/admin/client-excursion";
    }

    @IsAdmin
    @DeleteMapping("/delete/{id}")
    public String deleteClientExcursion(@PathVariable("id") int id) {
        clientExcursionService.delete(id);
        return "redirect:/admin/client-excursion";
    }
}
