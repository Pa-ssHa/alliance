package ru.kozelsk.alliance.controllers.admin.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.realty.admin.ClientRealty;
import ru.kozelsk.alliance.services.realty.admin.ClientRealtyService;
import ru.kozelsk.alliance.utils.annotations.IsAdmin;


@Controller
@RequestMapping("/admin/client-realty")
public class ClientRealtyController {

    private final ClientRealtyService clientRealtyService;

    @Autowired
    public ClientRealtyController(ClientRealtyService clientRealtyService) {
        this.clientRealtyService = clientRealtyService;
    }

    @IsAdmin
    @GetMapping()
    public String allClients(Model model) {
        model.addAttribute("clients", clientRealtyService.findAll());
        return "admin/client/realty/main";
    }


    @IsAdmin
    @GetMapping("/new")
    public String newClientRealty(Model model) {
        model.addAttribute("newClient", new ClientRealty());
        return "admin/client/realty/new";
    }

    @IsAdmin
    @PostMapping()
    public String createClientRealty(@ModelAttribute("newClient") ClientRealty clientRealty,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/client/realty/new";
        }

        if(clientRealty.getPhone() != null && !clientRealty.getPhone().startsWith("+7")) {
            clientRealty.setPhone(clientRealty.getPhone());
        }

        clientRealtyService.save(clientRealty);
        return "redirect:/admin/client-realty";
    }


    @IsAdmin
    @GetMapping("/edit/{id}")
    public String editClientRealty(@PathVariable("id") int id, Model model) {
        ClientRealty clientRealty = clientRealtyService.findOne(id);
        model.addAttribute("upClient", clientRealty);
        return "admin/client/realty/edit";
    }

    @IsAdmin
    @PostMapping("/{id}")
    public String updateClientRealty(@PathVariable("id") int id,
                                     @ModelAttribute("upClient") ClientRealty upClientRealty,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/client/realty/edit";
        }

        if(upClientRealty.getPhone() != null && !upClientRealty.getPhone().startsWith("+7")) {
            upClientRealty.setPhone(upClientRealty.getPhone());
        }

        clientRealtyService.update(upClientRealty, id);
        return "redirect:/admin/client-realty";
    }

    @IsAdmin
    @DeleteMapping("/delete/{id}")
    public String deleteClientRealty(@PathVariable("id") int id) {
        clientRealtyService.delete(id);
        return "redirect:/admin/client-realty";
    }
}
