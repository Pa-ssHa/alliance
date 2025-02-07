package ru.kozelsk.alliance.controllers.realty.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kozelsk.alliance.models.realty.admin.ClientRealty;
import ru.kozelsk.alliance.services.realty.FeedbackRealtyService;
import ru.kozelsk.alliance.services.realty.admin.ClientRealtyService;
import ru.kozelsk.alliance.services.users.MyUserDetailsService;
import ru.kozelsk.alliance.utils.model.Client;

@Controller
@RequestMapping("/realty/admin/client")
public class ClientRealtyController {

    private final MyUserDetailsService myUserDetailsService;
    private final FeedbackRealtyService feedbackRealtyService;
    private final ClientRealtyService clientRealtyService;

    @Autowired
    public ClientRealtyController(MyUserDetailsService myUserDetailsService, FeedbackRealtyService feedbackRealtyService, ClientRealtyService clientRealtyService) {
        this.myUserDetailsService = myUserDetailsService;
        this.feedbackRealtyService = feedbackRealtyService;
        this.clientRealtyService = clientRealtyService;
    }

    @GetMapping()
    public String allClients(Model model) {
        model.addAttribute("clients", clientRealtyService.findAll());
        return "realty/admin/client/main";
    }


    @GetMapping("/new")
    public String newClientRealty(Model model) {
        model.addAttribute("newClient", new ClientRealty());
        return "realty/admin/client/new";
    }

    @PostMapping()
    public String createClientRealty(@ModelAttribute("newClient") ClientRealty clientRealty,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "realty/admin/client/new";
        }

        if(clientRealty.getPhone() != null && !clientRealty.getPhone().startsWith("+7")) {
            clientRealty.setPhone("+7" + clientRealty.getPhone());
        }

        clientRealtyService.save(clientRealty);
        return "redirect:/realty/admin/client";
    }


    @GetMapping("/edit/{id}")
    public String editClientRealty(@PathVariable("id") int id, Model model) {
        ClientRealty clientRealty = clientRealtyService.findOne(id);
        model.addAttribute("upClient", clientRealty);
        return "realty/admin/client/edit";
    }

    @PostMapping("/{id}")
    public String updateClientRealty(@PathVariable("id") int id,
                                     @ModelAttribute("upClient") ClientRealty upClientRealty,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "realty/admin/client/edit";
        }

        if(upClientRealty.getPhone() != null && !upClientRealty.getPhone().startsWith("+7")) {
            upClientRealty.setPhone("+7" + upClientRealty.getPhone());
        }

        clientRealtyService.update(upClientRealty, id);
        return "redirect:/realty/admin/client";
    }

    @DeleteMapping("/{id}")
    public String deleteClientRealty(@PathVariable("id") int id) {
        clientRealtyService.delete(id);
        return "redirect:/realty/admin/client";
    }
}
